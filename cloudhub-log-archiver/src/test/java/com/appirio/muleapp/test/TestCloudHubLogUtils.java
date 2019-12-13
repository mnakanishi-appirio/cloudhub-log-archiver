package com.appirio.muleapp.test;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.appirio.muleapp.util.CloudHubLogUtils;

public class TestCloudHubLogUtils {
	
	private static String SAMPLE_LOG = "[2015-12-14 17:52:33.412] INFO    org.mule.module.launcher.application.DefaultMuleApplication [qtp18605202-31]: (t:null) \n" + 
			"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" + 
			"+ Initializing app 'test-test'                             +\n" + 
			"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" + 
			"[2015-12-14 17:52:34.065] INFO    com.mulesoft.ch.monitoring.MonitoringCoreExtension [qtp18605202-31]: (t:null) Monitoring enabled: true\n" + 
			"[2015-12-14 17:52:34.066] INFO    com.mulesoft.ch.monitoring.MonitoringCoreExtension [qtp18605202-31]: (t:null) Registering ping flow injector...";
	
	private static String[] SAMPLE_LOG_ARRAY = SAMPLE_LOG.split("\\n");
	
	@Test
	public void extractDiffTest1() {
		String case1 = "2015-12-15 02:52:34.066";
		String result1 = CloudHubLogUtils.extractDiff(case1, SAMPLE_LOG_ARRAY);
		String expected1 = StringUtils.EMPTY;
		assertEquals(expected1, result1);
	}
	
	@Test
	public void extractDiffTest2() {
		String case2 = "2015-12-15 02:52:34.065";
		String result2 = CloudHubLogUtils.extractDiff(case2, SAMPLE_LOG_ARRAY);
		String expected2 = "[2015-12-15 02:52:34.066] INFO    com.mulesoft.ch.monitoring.MonitoringCoreExtension [qtp18605202-31]: (t:null) Registering ping flow injector...\n";
		assertEquals(expected2, result2);
	}
	
	@Test
	public void extractDiffTest3() {
		String case3 = "2015-12-15 02:52:33.412";
		String result3 = CloudHubLogUtils.extractDiff(case3, SAMPLE_LOG_ARRAY);
		String expected3 = "[2015-12-15 02:52:34.065] INFO    com.mulesoft.ch.monitoring.MonitoringCoreExtension [qtp18605202-31]: (t:null) Monitoring enabled: true\n" + 
				"[2015-12-15 02:52:34.066] INFO    com.mulesoft.ch.monitoring.MonitoringCoreExtension [qtp18605202-31]: (t:null) Registering ping flow injector...\n";
		assertEquals(expected3, result3);
	}
	
	@Test
	public void getLatestLoggedTimeStrTest() {
		String result = CloudHubLogUtils.getLatestLoggedTimeStr(SAMPLE_LOG_ARRAY);
		String expected = "2015-12-15 02:52:34.066";
		assertEquals(expected, result);
	}

}
