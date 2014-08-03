package phc.processing;

import android.content.Context;
import phc.objects.DocResult;

public class BloodTestProcessor extends BaseDocProcessor {

	public BloodTestProcessor() {
		
	}
	
	@Override
	public boolean process(Context context, DocResult doc) {
		return true;
	}

}
