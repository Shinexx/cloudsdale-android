package org.cloudsdale.android.logic.test;

import junit.framework.Assert;

import org.cloudsdale.android.logic.CloudQuery;
import org.cloudsdale.android.logic.PersistentData;
import org.cloudsdale.android.models.Cloud;

import android.test.AndroidTestCase;

public class CloudQueryTest extends AndroidTestCase {
	private static String	url	= "http://192.168.2.4:3000/v1/clouds/";
	
	@Override
	protected void runTest() throws Throwable {
		super.runTest();
		PersistentData data = new PersistentData();
		
		CloudQuery query = new CloudQuery();
		query.execute(new String[] {url + "4f91c8458374e914c9000001"});
		
		while(PersistentData.getCloud("4f91c8458374e914c9000001") == null) {
			continue;
		}
		
		Cloud meta2 = PersistentData.getCloud("4f91c8458374e914c9000001");
		
		Assert.assertEquals(meta2.getName().toLowerCase(), "meta 2");
		Assert.assertEquals(meta2.getId(), "4f91c8458374e914c9000001");
	}
}