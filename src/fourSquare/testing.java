package fourSquare;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testing {

	@Before
	public void setUp() throws Exception {
		fourSquare.setupPropertyFiles();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void wrongURL() throws Exception{
		String clientId="RGIHNQWTCTBBQWAZEUZZKILDWF0EAQERVTXJUI23QU40ZZYJ";
		String clientSecret="ULCGZV4SQQNFZ21VPGK455SYCS4QSNHP1INNF1CZ21LPBF0E";
		String test = "https://api.foursquare.com/v2/venues/explore?"+"client_id="+clientId+"&client_secret="+clientSecret;
		URL url;

		url = new URL(test);

		String result = fourSquare.callFourSquare(url);
		//Check a result is JSON object
		
		String errorMsg = "An error has occurred, please try again later";
		assertEquals(errorMsg, result);
		// null checking
	
	}
	
	@Test
	public void correctURL() throws Exception{
		String clientId="RGIHNQWTCTBBQWAZEUZZKILDWF0EAQERVTXJUI23QU40ZZYJ";
		String clientSecret="ULCGZV4SQQNFZ21VPGK455SYCS4QSNHP1INNF1CZ21LPBF0E";
		String near = "streatham";
		String test = "https://api.foursquare.com/v2/venues/explore?"+"client_id="+clientId+"&client_secret="+clientSecret+"&v=20160601&near="+near;
		URL url;
		
		url = new URL(test);
		
		String result = fourSquare.callFourSquare(url);
		//Check a result is JSON object
		
		String code = result.substring(16, 19);
		assertEquals(code, "200");
		
		

	}

	
	@Test
	public void missingNearParameter() throws Exception{
		String clientId="RGIHNQWTCTBBQWAZEUZZKILDWF0EAQERVTXJUI23QU40ZZYJ";
		String clientSecret="ULCGZV4SQQNFZ21VPGK455SYCS4QSNHP1INNF1CZ21LPBF0E";
		String near = "";
		String test = "https://api.foursquare.com/v2/venues/explore?"+"client_id="+clientId+"&client_secret="+clientSecret+"&v=20160601&near="+near;
		URL url;

		url = new URL(test);
	
		
		String result = fourSquare.callFourSquare(url);
		
		
		String errorMsg = "An incorrect parameter has been entered, please try again";
		assertEquals(errorMsg, result);
		// null checking

		
	}


}
