package phc.processing;

import android.content.Context;
import phc.objects.DocResult;

public abstract class BaseDocProcessor {
	abstract public boolean process(Context context, DocResult doc);
}
