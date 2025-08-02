pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "KanbanBoards"
include(":app")
include(":api-service")
include(":auth")
include(":core")
include(":uikit")
include(":board")
include(":boards")
include(":invitation")
include(":profile")
include(":ticket")
include(":ticket-edit")
include(":ticket-create")
