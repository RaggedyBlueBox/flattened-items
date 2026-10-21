# Flattened Items

Renders items (and optionally blocks) as flat, like in older versions of the game —
which also means fewer quads for the GPU on every dropped item.

## Provenance

**Unofficial 26.3 port** by **RaggedyBlueBox**, forked from
[beamingblue/flat-items](https://github.com/beamingblue/flat-items) (`26.2` branch)
and updated with AI assistance. All rights retained under the original license.

- Original mod: **Flat Items** by BeamingBlue (MIT)
- Original concept: **Fast Items** by Noryea
- Includes the missing-ModMenu-entrypoint fix first made in
  [ThatCuteOne/flat-items](https://github.com/ThatCuteOne/flat-items)

## License

MIT — same as upstream. See upstream repository for full terms.

## What changed vs upstream 26.2

- Minecraft 26.2 → 26.3 (range `[26.3, 26.4)`), NeoForm 26.3-1
- Fabric Loader 0.19.5, Fabric API 0.164.0+26.3, ModMenu 21.0.0
- NeoForge 26.3.0.95 (consumer-facing build is Fabric)
- Added the `modmenu` entrypoint so the config screen registers
- Retargeted the spin-cancelling hook: 26.3's `ItemEntityRenderer.submit`
  rotates via `PoseStack.rotate(Axis, float)` (the old `mulPose(Quaternionfc)`
  overload is gone), and per-layer quads moved to immutable `ItemQuads`,
  so flattening now rebuilds via `setQuads(ItemQuads.split(...))`

## Build

```bash
export JAVA_HOME=/path/to/jdk-25
./gradlew :fabric:build
```

Output: `fabric/build/libs/flat_items-FABRIC-26.3-1.1.2.jar`
(rebrand: display name "Flattened Items", author RaggedyBlueBox;
mod id stays `flat_items` so it conflicts cleanly with any future upstream build
instead of double-loading.)

## Install

Drop the jar into `minecraft/mods/` on Minecraft 26.3 + Fabric Loader ≥ 0.19.5.
Requires Fabric API. Configure via Mod Menu.
