package com.google.typography.font.sfntly.table.opentype;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.table.opentype.component.GsubLookupType;

public class ExtensionSubst extends SubstSubtable {
  public static final int LOOKUP_TYPE_OFFSET = 0;
  public static final int LOOKUP_OFFSET_OFFSET = 2;

  final int lookupType;
  final int lookupOffset;

  public ExtensionSubst(ReadableFontData data, int base, boolean dataIsCanonical) {
    super(data, base, dataIsCanonical);
    if (format != 1) {
      throw new IllegalArgumentException("illegal extension format " + format);
    }
    lookupType = data.readUShort(base + headerSize() + LOOKUP_TYPE_OFFSET);
    lookupOffset = data.readULongAsInt(base + headerSize() + LOOKUP_OFFSET_OFFSET);
  }

  public SubstSubtable subTable() {
    ReadableFontData data = this.data.slice(lookupOffset);
    switch (GsubLookupType.forTypeNum(lookupType)) {
    case GSUB_LIGATURE:
      return new LigatureSubst(data, 0, dataIsCanonical);
    case GSUB_SINGLE:
      return new SingleSubst(data, 0, dataIsCanonical);
    case GSUB_MULTIPLE:
    case GSUB_ALTERNATE:
      return new MultipleSubst(data, 0, dataIsCanonical);
    case GSUB_CONTEXTUAL:
      return new ContextSubst(data, 0, dataIsCanonical);
    case GSUB_CHAINING_CONTEXTUAL:
      return new ChainContextSubst(data, 0, dataIsCanonical);
    default:
      throw new IllegalArgumentException("LookupType is " + lookupType);
    }
  }

  public static class Builder extends SubstSubtable.Builder<SubstSubtable> {
    public Builder() {
      super();
    }

    public Builder(ReadableFontData data, boolean dataIsCanonical) {
      super(data, dataIsCanonical);
    }

    @Override
    public SubstSubtable subBuildTable(ReadableFontData data) {
      return null;
    }
  }
}
