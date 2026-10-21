# Flattened Items

Renders dropped items flat, like the old days — and lighter on the GPU while doing it.

**Fork:** [RaggedyBlueBox/flattened-items](https://github.com/RaggedyBlueBox/flattened-items)
(upstream: [beamingblue/flat-items](https://github.com/beamingblue/flat-items))

## What it does

- Flattens the 3D models of items lying on the ground into 2D sprites
- Block items flatten too, out of the box (toggleable: Affect 3D)
- Optional: enlarge flattened 3D items, drop side faces
- Spin-cancelling so drops sit still and readable instead of rotating
- Everything configurable in-game via Mod Menu

## Requirements

- Minecraft 26.3, Fabric Loader ≥ 0.19.5, Fabric API, Java 25+

## Install

1. Download `flat_items-fabric-26.3-1.1.2.jar` from
   [Releases](https://github.com/RaggedyBlueBox/flattened-items/releases)
2. Drop it into `minecraft/mods/`
3. Configure via Mod Menu → Flattened Items

## Provenance

Unofficial 26.3 port by **RaggedyBlueBox**, forked from upstream's `26.2` branch
and updated with AI assistance. All rights retained under the original license.

- Original **Flat Items** by BeamingBlue (MIT)
- Original concept: **Fast Items** by Noryea
- ModMenu-entrypoint fix first made in
  [ThatCuteOne/flat-items](https://github.com/ThatCuteOne/flat-items)

## Porting notes (26.2 → 26.3)

- Dependency bumps: NeoForm 26.3-1, Loader 0.19.5, Fabric API 0.164.0,
  ModMenu 21.0.0, NeoForge 26.3.0.95 (build-only; shipped build is Fabric)
- `ItemEntityRenderer.submit` spins via `PoseStack.rotate(Axis, float)` now —
  the spin-cancelling hook follows it (the old `mulPose(Quaternionfc)`
  overload is gone in 26.3)
- Per-layer quads moved to immutable `ItemQuads`; flattening rebuilds layers
  through `setQuads(ItemQuads.split(...))` via a new `LayerRenderStateAccessor`
- Mod id stays `flat_items` so this build conflicts cleanly with any future
  upstream release instead of double-loading

## Build from source

```bash
export JAVA_HOME=/path/to/jdk-25
./gradlew :fabric:build
# → fabric/build/libs/flat_items-fabric-26.3-1.1.2.jar
```

## License

MIT, same as upstream.
