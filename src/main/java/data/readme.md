| Input    | XSLT | Output | Comments |
| -------- | ------- | ------- | ------- |  
| beckhams.exb  | exmaralda2isotei.xsl |beckhams_basic.xml| No tokenisation, no segs |
| beckhams_basic.xml  | normalize.xsl |beckhams_basic_normalized.xml|  |
| beckhams_basic_normalized.xml | tokenize.xsl | beckhams_tokenized.xml | |
| beckhams_tokenized.xml | segment.xsl | beckhams_segmented.xml | |
| beckhams_segmented.xml | time2tokenSpanReferences.xsl | beckhams_tokenspans.xml | |

| beckhams_tokenized.xml | detokenize.xsl | beckhams_detokenized.xml ||
