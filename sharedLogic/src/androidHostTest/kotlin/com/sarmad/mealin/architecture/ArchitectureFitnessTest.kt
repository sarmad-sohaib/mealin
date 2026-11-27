package com.sarmad.mealin.architecture

// Test strategy: docs/decisions/2026-11-27-test-strategy.md
// expect/actual: docs/decisions/2026-11-27-expect-actual-catalog-copy.md
// Scan: docs/decisions/2026-11-28-architecture-fitness-scan.md

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class ArchitectureFitnessTest {

    @Test
    fun commonMainDoesNotImportAndroidAndroidxOrAPlatformSdk() {
        val violations = forbiddenImportsUnder(commonMainKotlinDir(repoRoot()), repoRoot())

        assertEquals(emptyList(), violations)
    }

    @Test
    fun androidAndroidxAndPlatformImportsFailTheScan() {
        val source = listOf(
            "import android.os.Build",
            "import androidx.compose.runtime.Composable",
            "import platform.UIKit.UIDevice",
            "import kotlin.test.Test",
            "import com.android.volley.Request",
            "// import android.os.Build",
            "val sample = \"import platform.Foundation.NSString\"",
            "val raw = \"\"\"",
            "    import android.os.Looper",
            "\"\"\"",
            "/*",
            "import androidx.annotation.StringRes",
            "*/",
        ).joinToString(separator = "\n")

        assertEquals(
            listOf(
                "import android.os.Build",
                "import androidx.compose.runtime.Composable",
                "import platform.UIKit.UIDevice",
            ),
            forbiddenImportsIn(source),
        )
    }

    @Test
    fun wizardPlatformFilesDeclareExpectOrActual() {
        val root = repoRoot()
        val declared = expectOrActualFiles(root)

        assertEquals(emptyList(), wizardPlatformPaths.filter { it !in declared })
    }

    @Test
    fun allowlistIncludesTheWizardPlatformFiles() {
        val allowlist = readAllowlist(repoRoot())

        assertEquals(emptyList(), wizardPlatformPaths.filter { it !in allowlist })
    }

    @Test
    fun expectAndActualDeclarationsAreOnTheAllowlist() {
        val root = repoRoot()
        val outside = expectOrActualFiles(root).filter { it !in readAllowlist(root) }

        assertEquals(emptyList(), outside)
    }
}

private val wizardPlatformPaths = listOf(
    "sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/Platform.kt",
    "sharedLogic/src/androidMain/kotlin/com/sarmad/mealin/Platform.android.kt",
    "sharedLogic/src/iosMain/kotlin/com/sarmad/mealin/Platform.ios.kt",
)

private val forbiddenImportLine = Regex(
    """^\s*import\s+(android|androidx|platform)(?:\.|\s|$)""",
)

private val declarationKeywords = setOf(
    "fun",
    "class",
    "interface",
    "object",
    "val",
    "var",
    "typealias",
    "constructor",
)

private fun repoRoot(): File {
    val userDir = System.getProperty("user.dir") ?: error("user.dir is not set")
    var dir = File(userDir).absoluteFile
    while (true) {
        if (File(dir, "settings.gradle.kts").isFile && File(dir, "sharedLogic").isDirectory) {
            return dir
        }
        dir = dir.parentFile ?: error("Repo root not found from $userDir")
    }
}

private fun commonMainKotlinDir(root: File): File {
    return File(root, "sharedLogic/src/commonMain/kotlin")
}

private fun readAllowlist(root: File): Set<String> {
    return File(root, "sharedLogic/expect-actual-allowlist.txt")
        .readLines()
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()
}

private fun forbiddenImportsUnder(dir: File, root: File): List<String> {
    return dir.walkTopDown()
        .filter { it.isFile && it.extension == "kt" }
        .flatMap { file ->
            val path = file.relativeTo(root).invariantSeparatorsPath
            forbiddenImportsIn(file.readText()).map { importLine -> "$path: $importLine" }
        }
        .sorted()
        .toList()
}

private fun forbiddenImportsIn(source: String): List<String> {
    return stripCommentsAndStrings(source)
        .lineSequence()
        .map { it.trim() }
        .filter { forbiddenImportLine.containsMatchIn(it) }
        .toList()
}

private fun expectOrActualFiles(root: File): List<String> {
    return kotlinFiles(root)
        .filter { declaresExpectOrActual(it.readText()) }
        .map { it.relativeTo(root).invariantSeparatorsPath }
        .sorted()
        .toList()
}

private fun kotlinFiles(root: File): Sequence<File> {
    return root.walkTopDown()
        .onEnter { dir ->
            dir.name != "build" && dir.name != ".gradle" && dir.name != ".git"
        }
        .filter { it.isFile && it.extension == "kt" }
}

private fun declaresExpectOrActual(source: String): Boolean {
    return stripCommentsAndStrings(source).lineSequence().any { line ->
        val tokens = line.trim().split(Regex("""\s+"""))
        val marker = tokens.indexOfFirst { it == "expect" || it == "actual" }
        marker >= 0 && tokens.drop(marker + 1).any { token ->
            token.substringBefore('(').substringBefore('<') in declarationKeywords
        }
    }
}

private fun stripCommentsAndStrings(source: String): String {
    val out = StringBuilder()
    var index = 0
    while (index < source.length) {
        if (source.startsWith("\"\"\"", index)) {
            val end = closingRawString(source, index)
            appendBlankKeepingNewlines(out, source.substring(index, end))
            index = end
            continue
        }
        if (source.startsWith("//", index)) {
            val newline = source.indexOf('\n', index)
            index = if (newline < 0) source.length else newline
            continue
        }
        if (source.startsWith("/*", index)) {
            val end = closingBlockComment(source, index)
            appendBlankKeepingNewlines(out, source.substring(index, end))
            index = end
            continue
        }
        val current = source[index]
        if (current == '"' || current == '\'') {
            val end = closingQuoted(source, index, current)
            appendBlankKeepingNewlines(out, source.substring(index, end))
            index = end
            continue
        }
        out.append(current)
        index++
    }
    return out.toString()
}

private fun closingRawString(source: String, start: Int): Int {
    val end = source.indexOf("\"\"\"", start + 3)
    return if (end < 0) source.length else end + 3
}

private fun closingBlockComment(source: String, start: Int): Int {
    val end = source.indexOf("*/", start + 2)
    return if (end < 0) source.length else end + 2
}

private fun closingQuoted(source: String, start: Int, quote: Char): Int {
    var index = start + 1
    while (index < source.length) {
        if (source[index] == '\\') {
            index += 2
            continue
        }
        if (source[index] == quote) {
            return index + 1
        }
        if (source[index] == '\n') {
            return index
        }
        index++
    }
    return source.length
}

private fun appendBlankKeepingNewlines(out: StringBuilder, text: String) {
    text.forEach { char ->
        out.append(if (char == '\n') '\n' else ' ')
    }
}
