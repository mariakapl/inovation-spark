package phc.processing;

import phc.objects.DocResult;
import android.content.Context;

public interface IDocProcessor {
	boolean process(Context context, DocResult doc);
	void clear(Context context);
}
