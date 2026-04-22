package io.github.seyud.weave.core.model.module

data class RepoAuthor(
    val name: String,
    val link: String,
)

data class RepoReleaseAsset(
    val name: String,
    val downloadUrl: String,
    val size: Long,
    val downloadCount: Int,
)

data class RepoRelease(
    val name: String,
    val tagName: String,
    val publishedAt: String,
    val version: String,
    val versionCode: Int?,
    val isPrerelease: Boolean,
    val descriptionHtml: String,
    val releaseAssets: List<RepoReleaseAsset>,
) {
    val primaryZipAsset: RepoReleaseAsset?
        get() = releaseAssets.firstOrNull {
            it.downloadUrl.endsWith(".zip", ignoreCase = true) || it.name.endsWith(".zip", ignoreCase = true)
        }
}

data class RepoModuleLatestRelease(
    val name: String,
    val time: String,
    val version: String,
    val versionCode: Int?,
    val downloadUrl: String?,
)

data class RepoModuleSummary(
    val moduleId: String,
    val moduleName: String,
    val authors: List<RepoAuthor>,
    val summary: String,
    val metamodule: Boolean,
    val stargazerCount: Int,
    val latestRelease: RepoModuleLatestRelease?,
) {
    val displayName: String
        get() = moduleName.ifBlank { moduleId }

    val authorsText: String
        get() = authors.joinToString(", ") { it.name }
}

data class RepoModuleDetail(
    val moduleId: String,
    val moduleName: String,
    val url: String,
    val homepageUrl: String,
    val sourceUrl: String,
    val authors: List<RepoAuthor>,
    val summary: String,
    val readme: String,
    val readmeHtml: String,
    val metamodule: Boolean,
    val stargazerCount: Int,
    val releases: List<RepoRelease>,
) {
    val displayName: String
        get() = moduleName.ifBlank { moduleId }

    val authorsText: String
        get() = authors.joinToString(", ") { it.name }
}
