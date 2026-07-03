# AGENTS.md instructions

Do not provide progress updates while `gradlew` is running; when reporting, only summarize the result, failure reason, and necessary next steps.

## Repository Notes

- This is a Stonecutter multi-version Minecraft mod project.
- `settings.gradle.kts` currently registers multiple Fabric, Forge, and NeoForge versions; `vcsVersion` is `1.21.1-fabric`.
- The active project in `stonecutter.gradle.kts` is managed by Stonecutter. Do not manually edit the `stonecutter active "..."`
  line; use Stonecutter/Gradle tasks when switching versions.
- For long-term changes, edit the shared sources at the repository root instead of directly modifying generated outputs under `versions/.../build/generated/`.
- Before committing, confirm the active project is back to `vcsVersion` to avoid committing Stonecutter comment-switching noise.
- Do not use reflection as a replacement for Stonecutter conditional compilation or version branches.
- Do not replace strongly typed types with string IDs; prefer keeping and using Minecraft or project-provided strongly typed APIs, keys, registry types, enums, or value objects.

## CodeGraph

In repositories indexed by CodeGraph (a `.codegraph/` directory exists at the repo root), reach for it BEFORE grep/find or reading files when you need to understand or locate code:

- **MCP tools** (when available): `codegraph_explore` answers most code questions in one call — the relevant symbols' verbatim source plus the call paths between them. `codegraph_node` returns one symbol's source + callers, or reads a whole file with line numbers. If the tools are listed but deferred, load them by name via tool search.
- **Shell** (always works): `codegraph explore "<symbol names or question>"` and `codegraph node <symbol-or-file>` print the same output.

If there is no `.codegraph/` directory, skip CodeGraph entirely — indexing is the user's decision.

## Context7

Use the `ctx7` CLI to fetch current documentation whenever the user asks about a library, framework, SDK, API, CLI tool, or cloud service. This includes API syntax, configuration, version migration, library-specific debugging, setup instructions, and CLI tool usage.

Do not use Context7 for refactoring, writing scripts from scratch, debugging business logic, code review, or general programming concepts.

Steps:

1. Resolve library: `npx ctx7@latest library <name> "<user's question>"`
2. Pick the best match by exact name, relevance, snippet count, source reputation, and benchmark score.
3. Fetch docs: `npx ctx7@latest docs <libraryId> "<user's question>"`
4. Answer using the fetched documentation.

Must call `library` first unless the user provides a valid `/org/project` ID. Do not run more than 3 Context7 commands per question. Do not include sensitive information in queries.

Run Context7 CLI requests outside Codex's default sandbox. If a Context7 command fails with DNS or network errors, rerun it outside the sandbox instead of retrying inside the sandbox.
