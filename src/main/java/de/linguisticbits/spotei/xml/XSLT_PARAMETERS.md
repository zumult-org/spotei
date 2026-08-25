# spotei XSLT — externally settable parameters

Only top-level `<xsl:param>` declarations are listed (i.e. ones a Java caller can set via
`Transformer.setParameter(name, value)`). Parameters declared inside `<xsl:template>` /
`<xsl:function>` are internal implementation details and are **not** reachable from outside.

Files with no external parameters at all are listed at the bottom.

---

## conversion/

### exmaralda2isotei.xsl
| Param | Default | Notes |
|---|---|---|
| `LANGUAGE` | `xx` | ISO-ish language code to stamp into the output |
| `USE_XPOINTER` | `FALSE` | literal string `"TRUE"`/`"FALSE"` |

### exmaralda2isotei_eventToken.xsl
| Param | Default | Notes |
|---|---|---|
| `LANGUAGE` | `xx` | |
| `USE_XPOINTER` | `FALSE` | literal string `"TRUE"`/`"FALSE"` |

### folker2isotei.xsl
| Param | Default | Notes |
|---|---|---|
| `LANGUAGE` | `xx` | |
| `MAKE_INLINE_ATTRIBUTES` | `TRUE` | literal string `"TRUE"`/`"FALSE"` |
| `MAKE_STANDOFF_ANNOTATIONS` | `FALSE` | literal string `"TRUE"`/`"FALSE"` |
| `USE_XPOINTER` | `FALSE` | literal string `"TRUE"`/`"FALSE"` |
| `ENFORCE_SEG` | `TRUE` | literal string `"TRUE"`/`"FALSE"` |
| `SEGCOR_UNITS` | `FALSE` | literal string `"TRUE"`/`"FALSE"` |
| `TIMELINE_COPY` | computed from input `<timeline>` | complex default (XML fragment) — you'd rarely override this manually |

### isotei2exmaralda.xsl
| Param | Default | Notes |
|---|---|---|
| `TRANSCRIPTION_SYSTEM` | value of `//tei:transcriptionDesc/@ident` if present, else `GENERIC` | e.g. `cGAT` |

### isotei2exmaralda_keepTokens.xsl
| Param | Default | Notes |
|---|---|---|
| `TRANSCRIPTION_SYSTEM` | same as above | |

### isotei2folker.xsl
| Param | Default | Notes |
|---|---|---|
| `TRANSCRIPTION_SYSTEM` | `//tei:transcriptionDesc/@ident` | |

### isotei2vtt.xsl
| Param | Default | Notes |
|---|---|---|
| `TYPE` | `trans` | controls which annotation layer is rendered as WebVTT text |

### isotei2eaf.xsl
*(no external parameters — the only `<xsl:param>`s found are `ID_BASE`, all scoped inside templates)*

---

## processing/

### desegment.xsl
| Param | Default |
|---|---|
| `CONVENTION` | derived from `//tei:transcriptionDesc/@ident`, else `GENERIC` |

### detokenize.xsl
| Param | Default |
|---|---|
| `TRANSCRIPTION_SYSTEM` | derived from `//tei:transcriptionDesc/@ident`, else `GENERIC` |

### segment.xsl
| Param | Default |
|---|---|
| `TRANSCRIPTION_SYSTEM` | derived from `//tei:transcriptionDesc/@ident`, else `GENERIC` |

### tokenize.xsl
| Param | Default |
|---|---|
| `TRANSCRIPTION_SYSTEM` | derived from `//tei:transcriptionDesc/@ident`, else `GENERIC` |

### insertSentenceLayer.xsl
| Param | Default |
|---|---|
| `TYPE_OF_SENTENCE_SPANGRP` | `S` |

### interpolate.xsl
| Param | Default |
|---|---|
| `TIMELINE_START` | `//tei:when[1]/@xml:id` (first timepoint in the doc) |

### time2tokenSpanReferences.xsl
| Param | Default |
|---|---|
| `USE_XPOINTER` | `FALSE` |
| `SPAN_GRP_TYPE` | `.*` (regex matching span-group type) |

### token2timeSpanReferences.xsl
| Param | Default |
|---|---|
| `USE_XPOINTER` | `FALSE` |
| `SPAN_GRP_TYPE` | `.*` |

### attributes2spans.xsl, flattenSegHierarchy.xsl, normalize.xsl
*(no external parameters — all `<xsl:param>`s found are scoped inside templates)*

---

## output/

### isotei2html_annotations.xsl
| Param | Default | Notes |
|---|---|---|
| `START_ANNOTATION_BLOCK_ID` | — (must be supplied) | |
| `END_ANNOTATION_BLOCK_ID` | — (must be supplied) | |

### isotei2html_table.xsl
| Param | Default | Notes |
|---|---|---|
| `DROPDOWN` | `TRUE` | literal `"TRUE"`/`"FALSE"` |
| `PLAYPAUSEBUTTONS` | `TRUE` | literal `"TRUE"`/`"FALSE"` |
| `NUMBERING` | `TRUE` | literal `"TRUE"`/`"FALSE"` |
| `TRANSLATION` | `en;translation` | language;annotation-name pair |
| `FORM` | `trans` | which text form to render |
| `SHOW_NORM_DEV` | `TRUE` | literal `"TRUE"`/`"FALSE"` |
| `VIS_SPEECH_RATE` | `TRUE` | literal `"TRUE"`/`"FALSE"` |
| `VIS_PAUSE_INSIDE_U` | `TRUE` | literal `"TRUE"`/`"FALSE"` |
| `VIS_INCIDENT_NOT_TYPES` | — (must be supplied) | |
| `START_ANNOTATION_BLOCK_ID` | — (must be supplied) | |
| `END_ANNOTATION_BLOCK_ID` | — (must be supplied) | |
| `AROUND_ANNOTATION_BLOCK_ID` | — (must be supplied) | |
| `HOW_MUCH_AROUND` | — (must be supplied) | |
| `HIGHLIGHT_IDS_1` | — (must be supplied) | |
| `HIGHLIGHT_IDS_2` | — (must be supplied) | |
| `HIGHLIGHT_IDS_3` | — (must be supplied) | |
| `HIGHLIGHT_ANNOTATION_BLOCK` | — (must be supplied) | |
| `TOKEN_LIST_URL` | — (must be supplied) | |

### isotei2svg_density.xsl
| Param | Default | Notes |
|---|---|---|
| `START_ANNOTATION_BLOCK_ID` | — (must be supplied) | |
| `END_ANNOTATION_BLOCK_ID` | — (must be supplied) | |
| `AROUND_ANNOTATION_BLOCK_ID` | — (must be supplied) | |
| `HOW_MUCH_AROUND` | — (must be supplied) | |
| `SIZE` | `small` | |

### isotei2txt.xsl, isotei2html.xsl, annotationBlock2HTML.xsl
*(no `<xsl:param>` declarations at all)*

---

## Files with zero params anywhere
`addLowLevelAnchors.xsl`, `spans2attributes.xsl`, `removeStrayAnchors.xsl`,
`augmentTimeline.xsl`, `augmentTimeline_final.xsl`, `augmentTimeline2.xsl`,
`removeTimepointsWithoutAbsolute.xsl`


