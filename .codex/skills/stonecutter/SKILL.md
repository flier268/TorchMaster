---
name: stonecutter
description: Use when working on Stonecutter multi-version Minecraft mod projects, especially changing active versions, editing conditional Stonecutter comments, adding version or loader-specific code, or reviewing diffs caused by version switching.
---

# Stonecutter

Use this skill for Stonecutter multi-version Minecraft mod projects. Focus on three risks: wrong active version, malformed comment syntax, and putting version-specific logic in the wrong layer.

## Core Rules

- Read registered nodes and `vcsVersion` from `settings.gradle(.kts)`.
- Read the active version from `stonecutter.gradle(.kts)`; it controls root `src/` state and classpath.
- Switch active version before editing APIs that differ by Minecraft version or loader.
- Use Stonecutter tasks/actions, not manual edits to `stonecutter active "..." /* [SC] DO NOT EDIT */`.
- Prefer root shared sources; do not make lasting edits under `versions/.../build/generated/`.
- Keep loader entrypoints under `src/[fabric|forge|neoforge]/java` unless they need Stonecutter-transformed branches.
- Keep version branches in Minecraft adapter/content/lifecycle code, not `domain`, `port`, or shared content definitions.
- Keep shared ids and metadata in content definitions; do not duplicate them per loader.

Useful root tasks: `./gradlew "Set active project to PROJECT_NODE"`, `./gradlew "Refresh active project"`, `./gradlew "Reset active project"`. Prefer these over `stonecutterSwitchTo...` lifecycle/internal tasks.

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

For multi-version chains, order predicates oldest to newest; reserve `else` for the newest supported path:

```java
//? if <1.14 {
oldApi();
//?} elif <=1.16 {
api116();
//?} elif <1.21.1 {
api121();
//?} else {
latestApi();
//?}
```

For import blocks, prefer separate closed scopes over `if { ... } else { ... }` when one branch is currently commented out. Import branches are easy to leave in an invalid active/comment state such as an active `else` branch ending with `*///?}`.

Prefer:

```java
//? if <1.16.5 {
/*import old.Api;
*///?}
//? if >=1.16.5 {
import new.Api;
//?}
```

Lookup scope comments a range up to whitespace, or up to a marker after `>>`; `>>+` includes the marker:

```java
call(/*? if <NEW_API_VERSION >> ');'*/oldArg);
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

Loader constants are available in comments:

```java
//? if fabric {
fabricOnly();
//?} else if neoforge {
neoForgeOnly();
//?}
```

## Build Script Checks

`build.gradle.kts` is evaluated for every node. Follow the repo style: `stonecutter.eval(...)` plus `mod.isFabric`, `mod.isForge`, `mod.isNeoforge`.

```kotlin
stonecutter.current.project // project node, often version plus loader
stonecutter.current.version // logical Minecraft version
stonecutter.eval(stonecutter.current.version, ">=VERSION")
```

## Review Checklist

- Check `git diff` for accidental broad comment toggles.
- Refresh or switch Stonecutter after editing conditional comments.
- Before committing, reset active version to `vcsVersion`.
- For staged refactor phases, update the next-phase plan under `docs/refactor/`.
