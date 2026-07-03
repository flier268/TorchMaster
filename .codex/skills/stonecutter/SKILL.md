---
name: stonecutter
description: Use when working on Stonecutter multi-version Minecraft mod projects, especially changing active versions, editing conditional Stonecutter comments, adding version or loader-specific code, or reviewing diffs caused by version switching.
---

# Stonecutter

## Purpose

Use this skill for Stonecutter Gradle projects. It helps agents avoid the two common mistakes: editing the wrong generated/version state, and writing malformed Stonecutter comment syntax.

Primary docs checked:
- https://stonecutter.kikugie.dev/stonecutter/wiki/start/comments
- https://stonecutter.kikugie.dev/stonecutter/wiki/config/controller
- https://stonecutter.kikugie.dev/stonecutter/wiki/config/params
- https://stonecutter.kikugie.dev/stonecutter/wiki/start/settings

## Workflow

1. Inspect `settings.gradle(.kts)` for registered versions and `vcsVersion`.
2. Inspect `stonecutter.gradle(.kts)` for the active version. The active version controls the root `src/` state and classpath.
3. Switch active version before editing APIs that differ by Minecraft version or loader.
4. Edit root shared sources unless the task explicitly concerns generated files under `versions/.../build/generated/`.
5. Refresh or switch Stonecutter after editing conditional comments to ensure comment states are valid.
6. Before committing, reset to the configured VCS version to avoid committing comment-toggle noise.

For this repo:
- Active version is declared in `stonecutter.gradle.kts` as `stonecutter active "..." /* [SC] DO NOT EDIT */`; prefer Stonecutter tasks/actions over hand-editing it.
- Registered project nodes and `vcsVersion` must be read from `settings.gradle(.kts)` instead of copied into this skill.
- CLI examples: `./gradlew "Set active project to PROJECT_NODE"`, `./gradlew "Refresh active project"`, `./gradlew "Reset active project"`.
- Tasks named `stonecutterSwitchTo...` are lifecycle/internal tasks; prefer the human-readable root tasks or the IDE Stonecutter actions.

## Comment Syntax

Stonecutter instructions are comments starting with `?`, usually `//?` in Java/Kotlin. Compilers see comments or normal code; Stonecutter toggles the root source between enabled and commented states.

Closed scope for multi-line blocks:

```java
//? if >=NEW_API_VERSION {
newApi();
//?}
```

When inactive, Stonecutter wraps the block in a comment:

```java
//? if >=NEW_API_VERSION {
/*newApi();
*///?}
```

Line scope affects the next non-empty line only:

```java
//? if <NEW_API_VERSION
oldApi();
```

Branching supports `elif`, `else if`, and `else`. Every branch before the last must use closed scope:

```java
//? if <OLD_API_VERSION {
oldApi();
//?} elif <NEW_API_VERSION {
middleApi();
//?} else {
newApi();
//?}
```

Lookup scope comments a range up to whitespace by default, or up to a marker after `>>`. Use it for small inline API differences:

```java
call(/*? if <NEW_API_VERSION >> ');'*/oldArg);
```

Use `>>+` when the located marker should be included in the affected range:

```java
//? if >=NEW_API_VERSION >>+ '.'
SomeClass.staticMethod();
```

## Conditions

Version predicates:
- A bare semantic version means equality for that exact version.
- `=`, `!=`, `<`, `>`, `<=`, `>=` are direct comparisons.
- `~MAJOR.MINOR` matches the same major and minor version.
- `^MAJOR` matches the same major version.
- Multiple predicates are ANDed, such as `>=MIN_VERSION <MAX_VERSION`.
- Stonecutter does not use wildcard patch notation or `*`; prefer compatible-range operators or explicit ranges.

Logical operators:

```java
//? if fabric && >=VERSION
//? if forge || neoforge
//? if !(fabric && <VERSION)
//? if fabric && (VERSION_A || VERSION_B)
```

The implicit version target is the node's logical Minecraft version, not necessarily the project directory name. In this repo, project names include loader suffixes, while the logical version omits the loader suffix.

Constants and dependencies can be configured in Gradle and referenced from comments. Loader constants are common in multi-loader projects:

```java
//? if fabric {
fabricOnly();
//?} else if neoforge {
neoForgeOnly();
//?}
```

## Build Script Checks

In `build.gradle.kts`, the shared build script is evaluated for every Stonecutter node.

Use:

```kotlin
stonecutter.current.project // project node, often version plus loader
stonecutter.current.version // logical Minecraft version
stonecutter.eval(stonecutter.current.version, ">=VERSION")
```

Or the `sc` alias when the project uses it:

```kotlin
if (sc.current.parsed >= "VERSION") {
    // version-specific Gradle logic
}
```

For this repo, existing build logic uses `stonecutter.eval(...)` and `mod.isFabric`, `mod.isForge`, `mod.isNeoforge`; follow that local style.

## Review Checklist

Before finishing Stonecutter work:
- Check `git diff` for accidental broad comment toggles.
- Verify the active version is the intended one for development, and reset to `vcsVersion` before commit-ready work.
- Ensure branch chains use closed scopes except possibly the final branch.
- Prefer narrow predicates and include loader constants when APIs differ by loader.
- Do not manually edit generated source under `versions/.../build/generated/` for lasting changes.
