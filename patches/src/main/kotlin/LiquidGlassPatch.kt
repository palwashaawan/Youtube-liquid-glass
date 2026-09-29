package app.revanced.patches.youtube.layout.liquidglass

import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch

val liquidGlassResourcesPatch = resourcePatch(
    description = "Adds glass colors and drawable resources.",
) {
    execute {
        document("res/values/colors.xml").use { doc ->
            val root = doc.documentElement
            fun color(name: String, value: String) {
                val e = doc.createElement("color")
                e.setAttribute("name", name)
                e.textContent = value
                root.appendChild(e)
            }
            color("lg_glass_fill", "#33FFFFFF")
            color("lg_glass_fill_top", "#66FFFFFF")
            color("lg_glass_stroke", "#80FFFFFF")
        }

        get("res").resolve("drawable/lg_glass_bg.xml").writeText(
            """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <gradient
        android:angle="90"
        android:startColor="@color/lg_glass_fill"
        android:endColor="@color/lg_glass_fill_top" />
    <stroke android:width="1dp" android:color="@color/lg_glass_stroke" />
    <corners android:radius="28dp" />
</shape>
"""
        )
    }
}

val liquidGlassPatch = bytecodePatch(
    name = "Liquid glass",
    description = "Gives the bottom navigation bar a liquid glass look.",
) {
    dependsOn(liquidGlassResourcesPatch)
    compatibleWith("com.google.android.youtube")

    execute {
        // TODO: fingerprint the nav bar class for your YouTube version,
        // then call:
        // invoke-static { vContent, vBar }