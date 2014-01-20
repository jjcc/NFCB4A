// THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS 
// WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE 
// TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY 
// DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS 
// ARISING FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS 
// OF THE CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.


package com.nfc.common;


//import android.util.Log;

public class NDEFMessages {
	
	
	//***********************************************************************/
	 //* The function Convert a String to a Byte Array NDEF message.
	 //* The Byte Array NDEF message contains both [CC field and TLV field]
	 //* from magic number -> 0xE1 to Terminator TLV -> 0xFE
	 //* Example : "salut" -> { 0XE1 ...................... 0XFE }
	 //***********************************************************************/
	public static byte[] ConvertStringToNDEF_Text_ByteArray(String stringToConvert,NfcvData ma)
	{
		
		String strNDEFtobeWritten="";
		String strNDEFlen = "";
		boolean boolLenOneByteRecord;		
		
		byte[] ConvertedString = new byte[stringToConvert.length()];
		
		//translate data to write
		char[]CharArray = stringToConvert.toCharArray();
		String StringRecord="";
		for(int i=0; i<stringToConvert.length();i++)
		{
			StringRecord += Helper.ConvertHexByteToString((byte)CharArray[i]); // Payload
		}
		
		//int PayloadLenght = CharArray.length + 3;
		//if (PayloadLenght>=255)
		//	PayloadLenght = 255;
		
		int maxMessageLenght = (Helper.ConvertStringToInt((ma.getMemorySize().replace(" ", ""))))*4; 
		int blockSize = Helper.ConvertStringToHexByte(ma.getBlockSize());
		int memorySize = 0;
		
		memorySize = Helper.ConvertStringToInt(ma.getMemorySize().replace(" ", ""));	
				
		if(stringToConvert.length() <= maxMessageLenght)
		{	
			
			strNDEFtobeWritten = "FE";			
			
			strNDEFtobeWritten = StringRecord + strNDEFtobeWritten;	//text
			strNDEFtobeWritten = "02656E" + strNDEFtobeWritten;	//status byte + language code
			
			int iNDEFtobeWrittenLen = (strNDEFtobeWritten.replace(" ","").length() / 2)-1;			
			if (iNDEFtobeWrittenLen < 0xFF)	//1 byte length
			{
				strNDEFlen = Helper.ConvertHexByteToString((byte)iNDEFtobeWrittenLen);
				boolLenOneByteRecord = true;
			}
			else
			{
				strNDEFlen = "0000" + Helper.ConvertHexByteArrayToString(Helper.ConvertIntTo2bytesHexaFormat(iNDEFtobeWrittenLen));				//4 byte length
				boolLenOneByteRecord = false;
			}
			strNDEFtobeWritten = "54" + strNDEFtobeWritten;	//RTD_TEXT
			strNDEFtobeWritten = strNDEFlen + strNDEFtobeWritten;	//RTD_TEXT
			strNDEFtobeWritten = "01" + strNDEFtobeWritten;	//RTD_TEXT
			
			if (boolLenOneByteRecord == true)
				strNDEFtobeWritten = "D1" + strNDEFtobeWritten;	//RTD_TEXT
			else
				strNDEFtobeWritten = "C1" + strNDEFtobeWritten;	//RTD_TEXT
			
			int iNDEFlen = (strNDEFtobeWritten.replace(" ","").length() / 2)-1;
			if (iNDEFlen < 0xFF)	//1 byte length		
				strNDEFlen = Helper.ConvertHexByteToString((byte)iNDEFlen);
			else
				strNDEFlen = "FF" + Helper.ConvertHexByteArrayToString(Helper.ConvertIntTo2bytesHexaFormat(iNDEFlen));				//4 byte length				//FF + 2 byte length
			strNDEFtobeWritten = strNDEFlen + strNDEFtobeWritten;	
			
			strNDEFtobeWritten = "03" + strNDEFtobeWritten;	//RTD_TEXT

			//CC3
			byte CC3 = (byte) 0x00;		
			if (ma.isMultipleReadSupported())			
				CC3 |= (byte)0x01; 	//bit0= 1:support MultipleReadBlocks 0:no
			if (ma.isMemoryExceed2048bytesSize())			
				CC3 |= (byte)0x04; 	//bit2= 1:Memory exceed 2048 bytes 0:no
			strNDEFtobeWritten = Helper.ConvertHexByteToString(CC3) + strNDEFtobeWritten;
			//CC2
			byte CC2 = (byte) 0x00;
			if (((memorySize+1)*(blockSize+1)) >= 2048)		//memory size = nb blocks *4 == nb bytes
				CC2 = (byte)0xFF; //CC2
			else
				CC2 = (byte) (((memorySize+1) * (blockSize+1))/8); //CC2
			strNDEFtobeWritten = Helper.ConvertHexByteToString(CC2) + strNDEFtobeWritten;
			//CC1
			strNDEFtobeWritten = "40" + strNDEFtobeWritten;
			//CC0
			strNDEFtobeWritten = "E1" + strNDEFtobeWritten;
			
			strNDEFtobeWritten = strNDEFtobeWritten.replace(" ", "");			
			
			ConvertedString = Helper.ConvertStringToHexBytesArray(strNDEFtobeWritten);
							
		}
		else
		{
			ConvertedString = null;
			return ConvertedString;
		}
		
		return ConvertedString;
	}
	
	//***********************************************************************/
	 //* The function Convert a String to a Byte Array NDEF url.
	 //* The Byte Array NDEF url contains both [CC field and TLV field]
	 //* from magic number -> 0xE1 to Terminator TLV -> 0xFE
	 //* Example : "http://www.st.com" -> { 0XE1 ...................... 0XFE }
	 //***********************************************************************/
	public static byte[] ConvertStringToNDEF_Url_ByteArray(String stringToConvert,NfcvData ma)
	{
		byte[] ConvertedString = new byte[stringToConvert.length()+12];

		int maxMessageLenght = (Helper.ConvertStringToInt((ma.getMemorySize().replace(" ", ""))))*4; 
		
		int memorySize = 0;		
		memorySize = Helper.ConvertStringToInt(ma.getMemorySize().replace(" ", ""));
		int blockSize = Helper.ConvertStringToHexByte(ma.getBlockSize());
		
		String strNDEFtobeWritten="";
		String strNDEFlen = "";
		boolean boolLenOneByteRecord;	
		
		String strPrefixData = "";
		String strRecord = "";		
		
		if(stringToConvert.length() <= maxMessageLenght)
		{
			
			if (stringToConvert.toLowerCase().startsWith("http://www."))
			{
				strPrefixData = "01";
				strRecord = stringToConvert.toLowerCase().replace("http://www.", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("https://www."))
			{
				strPrefixData = "02";
				strRecord = stringToConvert.toLowerCase().replace("http://www.", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("http://"))
			{
				strPrefixData = "03";				
				strRecord = stringToConvert.toLowerCase().replace("http://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("https://"))
			{
				strPrefixData = "04";
				strRecord = stringToConvert.toLowerCase().replace("https://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("tel:"))
			{
				strPrefixData = "05";	
				strRecord = stringToConvert.toLowerCase().replace("tel:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("mailto:"))
			{
				strPrefixData = "06";
				strRecord = stringToConvert.toLowerCase().replace("mailto:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("ftp://anonymous:anonymous@"))
			{
				strPrefixData = "07";				
				strRecord = stringToConvert.toLowerCase().replace("ftp://anonymous:anonymous@", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("ftp://ftp."))
			{
				strPrefixData = "08";	
				strRecord = stringToConvert.toLowerCase().replace("ftp://ftp.", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("ftps://"))
			{
				strPrefixData = "09";	
				strRecord = stringToConvert.toLowerCase().replace("ftps://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("sftp://"))
			{
				strPrefixData = "0A";	
				strRecord = stringToConvert.toLowerCase().replace("sftp://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("smb://"))
			{
				strPrefixData = "0B";	
				strRecord = stringToConvert.toLowerCase().replace("smb://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("nfs://"))
			{
				strPrefixData = "0C";	
				strRecord = stringToConvert.toLowerCase().replace("nfs://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("ftp://"))
			{
				strPrefixData = "0D";	
				strRecord = stringToConvert.toLowerCase().replace("ftp://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("dav://"))
			{
				strPrefixData = "0E";	
				strRecord = stringToConvert.toLowerCase().replace("dav://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("news://"))
			{
				strPrefixData = "0F";	
				strRecord = stringToConvert.toLowerCase().replace("news://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("telnet://"))
			{
				strPrefixData = "10";	
				strRecord = stringToConvert.toLowerCase().replace("telnet://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("imap://"))
			{
				strPrefixData = "11";	
				strRecord = stringToConvert.toLowerCase().replace("imap://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("rtsp://"))
			{
				strPrefixData = "12";	
				strRecord = stringToConvert.toLowerCase().replace("rtsp://", "");
			}
			//else if (stringToConvert.toLowerCase().startsWith("urn:"))
			//{
			//	prefixData = "13";			
			//	strRecord = stringToConvert.replace("urn:", "");
			//}
			else if (stringToConvert.toLowerCase().startsWith("pop:"))
			{
				strPrefixData = "14";	
				strRecord = stringToConvert.replace("pop:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("sip:"))
			{
				strPrefixData = "15";	
				strRecord = stringToConvert.replace("sip:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("sips:"))
			{
				strPrefixData = "16";	
				strRecord = stringToConvert.replace("sips:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("tftp:"))
			{
				strPrefixData = "17";	
				strRecord = stringToConvert.replace("tftp:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("btspp://"))
			{
				strPrefixData = "18";	
				strRecord = stringToConvert.replace("btspp://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("bttl2cap://"))
			{
				strPrefixData = "19";	
				strRecord = stringToConvert.replace("bttl2cap://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("btgoep://"))
			{
				strPrefixData = "1A";	
				strRecord = stringToConvert.replace("btgoep://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("tcpobex://"))
			{
				strPrefixData = "1B";	
				strRecord = stringToConvert.replace("tcpobex://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("irdaobex://"))
			{
				strPrefixData = "1C";	
				strRecord = stringToConvert.replace("irdaobex://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("file://"))
			{
				strPrefixData = "1D";	
				strRecord = stringToConvert.replace("file://", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("urn:epc:id:"))
			{
				strPrefixData = "1E";	
				strRecord = stringToConvert.replace("urn:epc:id:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("urn:epc:tag:"))
			{
				strPrefixData = "1F";	
				strRecord = stringToConvert.replace("urn:epc:tag:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("urn:epc:pat:"))
			{
				strPrefixData = "20";	
				strRecord = stringToConvert.replace("urn:epc:pat:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("urn:epc:raw"))
			{
				strPrefixData = "21";	
				strRecord = stringToConvert.toLowerCase().replace("urn:epc:raw", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("urn:epc:"))
			{
				strPrefixData = "22";	
				strRecord = stringToConvert.toLowerCase().replace("urn:epc:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("urn:nfc:"))
			{
				strPrefixData = "23";	
				strRecord = stringToConvert.toLowerCase().replace("urn:nfc:", "");
			}
			else if (stringToConvert.toLowerCase().startsWith("urn:"))
			{
				strPrefixData = "13";				
				strRecord = stringToConvert.toLowerCase().replace("urn:", "");
			}
			else
			{
				strPrefixData = "00";
				strRecord = stringToConvert.toLowerCase();
			}

			//translate data to write
			char[] CharArray = strRecord.toCharArray();
			String StringRecord="";
			for(int i=0; i<strRecord.length();i++)
			{
				StringRecord += Helper.ConvertHexByteToString((byte)CharArray[i]); // Payload
			}
						
			strNDEFtobeWritten = "FE";						
			strNDEFtobeWritten = StringRecord + strNDEFtobeWritten;	//text
			strNDEFtobeWritten = strPrefixData + strNDEFtobeWritten;	//prefix if any
			
			strNDEFtobeWritten = "55" + strNDEFtobeWritten;	//status byte + language code
			
			
			int iNDEFtobeWrittenLen = strRecord.length() + 1;			
			if (iNDEFtobeWrittenLen < 0xFF)	//1 byte length
			{
				strNDEFlen = Helper.ConvertHexByteToString((byte)iNDEFtobeWrittenLen);
				boolLenOneByteRecord = true;
			}
			else
			{
				strNDEFlen = "0000" + Helper.ConvertHexByteArrayToString(Helper.ConvertIntTo2bytesHexaFormat(iNDEFtobeWrittenLen));				//4 byte length
				boolLenOneByteRecord = false;
			}
			strNDEFtobeWritten = strNDEFlen + strNDEFtobeWritten;
			
			strNDEFtobeWritten = "01" + strNDEFtobeWritten;	//status byte + language code
						
			if (boolLenOneByteRecord == true)
				strNDEFtobeWritten = "D1" + strNDEFtobeWritten;	//RTD_TEXT
			else
				strNDEFtobeWritten = "C1" + strNDEFtobeWritten;	//RTD_TEXT
			
			int iNDEFlen = (strNDEFtobeWritten.replace(" ","").length() / 2)-1;
			if (iNDEFlen < 0xFF)	//1 byte length		
				strNDEFlen = Helper.ConvertHexByteToString((byte)iNDEFlen);
			else
				strNDEFlen = "FF" + Helper.ConvertHexByteArrayToString(Helper.ConvertIntTo2bytesHexaFormat(iNDEFlen));				//4 byte length				//FF + 2 byte length
			strNDEFtobeWritten = strNDEFlen + strNDEFtobeWritten;	
			
			strNDEFtobeWritten = "03" + strNDEFtobeWritten;	//RTD_TEXT

			//CC3
			byte CC3 = (byte) 0x00;		
			if (ma.isMultipleReadSupported())			
				CC3 |= (byte)0x01; 	//bit0= 1:support MultipleReadBlocks 0:no
			if (ma.isMemoryExceed2048bytesSize())			
				CC3 |= (byte)0x04; 	//bit2= 1:Memory exceed 2048 bytes 0:no
			strNDEFtobeWritten = Helper.ConvertHexByteToString(CC3) + strNDEFtobeWritten;
			//CC2
			byte CC2 = (byte) 0x00;
			if (((memorySize+1)*(blockSize+1)) >= 2048)		//memory size = nb blocks *4 == nb bytes
				CC2 = (byte)0xFF; //CC2
			else
				CC2 = (byte) (((memorySize+1) * (blockSize+1))/8); //CC2
			strNDEFtobeWritten = Helper.ConvertHexByteToString(CC2) + strNDEFtobeWritten;
			//CC1
			strNDEFtobeWritten = "40" + strNDEFtobeWritten;
			//CC0
			strNDEFtobeWritten = "E1" + strNDEFtobeWritten;
			
			strNDEFtobeWritten = strNDEFtobeWritten.replace(" ", "");			
			
			ConvertedString = Helper.ConvertStringToHexBytesArray(strNDEFtobeWritten);
			
		}
		
		else
		{
			return ConvertStringToNDEF_Url_ByteArray("message too long",ma);
		}
		
		return ConvertedString;
	}
	
	//***********************************************************************/
		 //* The function Convert a Byte Array NDEF Text to a String .
		 //* Example :  { 0XE1 ...................... 0XFE } -> "hello"
		 //***********************************************************************/
		public static String ConvertNDEF_ByteArrayToString_Adapted(byte[] ByteArrayToConvert)					
		{
			int intByteArrayCounter = 0 ;
			int intLField = 0;
			int inttimeout=0;
			boolean boolLastTLVblock=false;
			String ConvertedByteArray = "";
			
			//CC Field
			//ByteArrayToConvert[0] = CC0 : E1
			intByteArrayCounter++;
			//ByteArrayToConvert[1] = CC1 : 40
			intByteArrayCounter++; 
			//ByteArrayToConvert[2] = CC2 : FF
			intByteArrayCounter++;
			//ByteArrayToConvert[3] = CC3 : 00
			intByteArrayCounter++;
			
			//TLV
			//ByteArrayToConvert[4] = TField : 03
				//=00 : "Used to Padding"
				//=01 : "Defines details of the lock bits"
				//=02 : "Identify reserved memory areas"
				//=03 : "Contains NDEF message" 			<-- NDEF
				//=FD : "Tag proprietary information"
				//=FE : "Last TLV block in the data area"	

			//TLV : 01 = "Defines details of the lock bits"
			//if(ByteArrayToConvert[4]==(byte)0x01)
			if(ByteArrayToConvert[intByteArrayCounter]==(byte)0x01)
			{
				intByteArrayCounter++;
				//ByteArrayToConvert[5] Length

				intByteArrayCounter++;
				//ByteArrayToConvert[6] Length
				
				intByteArrayCounter++;
				//ByteArrayToConvert[7] Length

				intByteArrayCounter++;
				//ByteArrayToConvert[8] Length
				
				intByteArrayCounter++; //[9]
			}
			
			//TLV : 03 = "Contains NDEF message"
			//if(ByteArrayToConvert[4]==(byte)0x03)
			if(ByteArrayToConvert[intByteArrayCounter]==(byte)0x03)
			{
				intByteArrayCounter++;
				//if(ByteArrayToConvert[5] == (byte)0xFF)
				if(ByteArrayToConvert[intByteArrayCounter] == (byte)0xFF)
				{
					intByteArrayCounter++;
					intByteArrayCounter++;
					intLField = (((int)(ByteArrayToConvert[intByteArrayCounter-1] & 0xFF) << 8) | (int)(ByteArrayToConvert[intByteArrayCounter] & 0xFF));
				}
				else
				{	
					intLField = (byte)ByteArrayToConvert[intByteArrayCounter] & 0xFF;
				}					
		         
				intLField--;
				
				boolean boolFirstRecord;
				boolean boolLastRecord;
				boolean boolInitialMiddeRecord;
				boolean boolPayloadInOneByte;
				boolean boolIDlengthPresent;
				byte bytRecordHeaderByte;
				byte bytStructureRecord;
				
				do
				{					
			         
					intByteArrayCounter++;
					intLField--;
		            bytRecordHeaderByte = ByteArrayToConvert[intByteArrayCounter];

		            if ((bytRecordHeaderByte & (byte)0x80) == (byte)0x80)
		                boolFirstRecord = true;
		            else	            	
		                boolFirstRecord = false;            
		            
		            if ((bytRecordHeaderByte & (byte)0x40) == (byte)0x40)
		                boolLastRecord = true;
		            else
		            	boolLastRecord = false;
		            
		            if ((bytRecordHeaderByte & (byte)0x20) == (byte)0x20)
		            	boolInitialMiddeRecord = true;
		            else
		            	boolInitialMiddeRecord = false;
		
		            if ((bytRecordHeaderByte & (byte)0x10) == (byte)0x10)
		            	boolPayloadInOneByte = true;
		            else
		            	boolPayloadInOneByte = false;
		
		            if ((bytRecordHeaderByte & (byte)0x08) == (byte)0x08)
		            	boolIDlengthPresent = true;
		            else
		            	boolIDlengthPresent = false;            //--> Pas encore g
		            bytStructureRecord = (byte)bytRecordHeaderByte;	
		            //bytStructureRecord &= (byte)0x03; Error !
		            bytStructureRecord &= (byte)0x07;
		            		
		            //If (iStructureRecord = TNF_WELL_KNOWN Or iStructureRecord = TNF_ABSOLUTE_URI) Then  'NFC forum supported message
		            if ((bytStructureRecord == 0x01) || (bytStructureRecord == 0x03))
		            {
		            	int intNDEFtypeLength;
		            	intByteArrayCounter++;
		            	intLField--;
		            	//intNDEFtypeLength = ByteArrayToConvert[intByteArrayCounter];
		            	intNDEFtypeLength = (int)(ByteArrayToConvert[intByteArrayCounter] & 0xFF);
		            	
		            	int intNDEFPayloadLength;
		            	
		            	if (boolPayloadInOneByte == true)
		            	{	            	
		            		intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength = (int) (ByteArrayToConvert[intByteArrayCounter] & 0xFF);
		            	}
		            	else
		            	{
		            		intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength = (int) (ByteArrayToConvert[intByteArrayCounter] & 0xFF);
			            	intNDEFPayloadLength = intNDEFPayloadLength << 8;
			            	intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength += (int) (ByteArrayToConvert[intByteArrayCounter] & 0xFF);
			            	intNDEFPayloadLength = intNDEFPayloadLength << 8;
			            	intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength += (int) (ByteArrayToConvert[intByteArrayCounter] & 0xFF);
			            	intNDEFPayloadLength = intNDEFPayloadLength << 8;
			            	intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength += (int) (ByteArrayToConvert[intByteArrayCounter] & 0xFF);
		            	}
		            	
		                //Record Type
		                int intNDEFRecordType = 0;
		                for(int i=0; i<intNDEFtypeLength; i++)
		                {
		            		intByteArrayCounter++;
			            	intLField--;
			            	intNDEFRecordType = intNDEFRecordType << 8;
			            	intNDEFRecordType += ByteArrayToConvert[intByteArrayCounter];
		                }
		                
		                //Const RTD_TEXT = "54"                       '"T" TEXT of TNF_WELL_KNOWN
		                //Const RTD_URI = "55"                        '"U"
		                //Const RTD_SMART_POSTER = "5370"             '"sp"
		                //Const RTD_ALTERNATIVE_CARRIER = "6163"      '"ac"
		                //Const RTD_HANDOVER_CARRIER = "4863"         '"Hc"
		                //Const RTD_HANDOVER_REQUEST = "4872"         '"Hr"
		                //Const RTD_HANDOVER_SELECT = "4873"          '"Hs"	                
		            
		                if (intNDEFRecordType == 0x54) //Const RTD_TEXT = "54"
		                { 	
		                	ConvertedByteArray += "type:TEXT " + (char)0x0D + (char)0x0A;
		                			
		                	int intNDEFTEXTstatusByte;
		                	boolean boolTextEncodingUTF_8;
		            		intByteArrayCounter++;
			            	intLField--;
			            	//size of language field
			            	intNDEFTEXTstatusByte = ByteArrayToConvert[intByteArrayCounter];                        
			            	
	                        //NDEF encoding format
			            	if ((intNDEFTEXTstatusByte & 0x80) == 0x80) //bit 7 = text encoded
	                        	boolTextEncodingUTF_8 = true;
	                        else
	                        	boolTextEncodingUTF_8 = false;
	                        
			            	//NDEF language
	                        intNDEFTEXTstatusByte &= (byte)0x3F;
	                        if (intNDEFTEXTstatusByte > 0)
	                        {
		                        for(int i=0; i<intNDEFTEXTstatusByte; i++)
		                        {
		                        	intByteArrayCounter++;
		                        	intLField--;
		                        	//language : ByteArrayToConvert[intByteArrayCounter];
		                        	//ConvertedByteArray += (char)ByteArrayToConvert[intByteArrayCounter];
		                        }		                            
	                        }

	                        //NDEF text
	                        for(int i=intNDEFTEXTstatusByte; i<intNDEFPayloadLength-1; i++)
	                        {
	    	            		intByteArrayCounter++;
	    		            	intLField--;
	    		            	ConvertedByteArray += (char)ByteArrayToConvert[intByteArrayCounter];
	                        }	                                                                  
		                }	
		                
		                else if (intNDEFRecordType == 0x55) //Const RTD_URI = "55"
		                {
		                	ConvertedByteArray += "type:URI " + (char)0x0D + (char)0x0A;
		                	//URI identifier
		                	int intNDEFUriIdentifier;
		                	intByteArrayCounter++;
			            	intLField--;
	                        intNDEFUriIdentifier = (int)(ByteArrayToConvert[intByteArrayCounter] & 0xFF);                        
	                        
	                        switch (intNDEFUriIdentifier)
	                        {
		                        case 0x00:
		                        	ConvertedByteArray += "";     //NA
	                        		break;
		                        case 0x01:
		                        	ConvertedByteArray += "http://www.";
		                        	break;
		                        case 0x02:
		                        	ConvertedByteArray += "https://www.";
		                            break;
		                        case 0x03:
		                        	ConvertedByteArray += "http://";
		                            break;
		                        case 0x04:
		                        	ConvertedByteArray += "https://";
		                            break;
		                        case 0x05:
		                        	ConvertedByteArray += "tel:";
		                            break;
		                        case 0x06:
		                        	ConvertedByteArray += "mailto:";
		                            break;
		                        case 0x07:
		                        	ConvertedByteArray += "ftp://anonymous:anonymous@";
		                            break;
		                        case 0x08:
		                        	ConvertedByteArray += "ftp://ftp.";
		                            break;
		                        case 0x09:
		                        	ConvertedByteArray += "ftps://";
		                            break;
		                        case 0x0A:
		                        	ConvertedByteArray += "sftp://";
		                            break;
		                        case 0x0B:
		                        	ConvertedByteArray += "smb://";
		                            break;
		                        case 0x0C:
		                        	ConvertedByteArray += "nfs://";
		                            break;
		                        case 0x0D:
		                        	ConvertedByteArray += "ftp://";
		                            break;
		                        case 0x0E:
		                        	ConvertedByteArray += "dav://";
		                            break;
		                        case 0x0F:
		                        	ConvertedByteArray += "news://";
		                            break;
		                        case 0x10:
		                        	ConvertedByteArray += "telnet://";
		                            break;
		                        case 0x11:
		                        	ConvertedByteArray += "imap:";
		                            break;
		                        case 0x12:
		                        	ConvertedByteArray += "rtsp://";
		                            break;
		                        case 0x13:
		                        	ConvertedByteArray += "urn:";
		                            break;
		                        case 0x14:
		                        	ConvertedByteArray += "pop:";
		                            break;
		                        case 0x15:
		                        	ConvertedByteArray += "sip:";
		                            break;
		                        case 0x16:
		                        	ConvertedByteArray += "sips:";
		                            break;
		                        case 0x17:
		                        	ConvertedByteArray += "tftp:";
		                            break;
		                        case 0x18:
		                        	ConvertedByteArray += "btspp://";
		                            break;
		                        case 0x19:
		                        	ConvertedByteArray += "btl2cap://";
		                            break;
		                        case 0x1A:
		                        	ConvertedByteArray += "btgoep://";
		                            break;
		                        case 0x1B:
		                        	ConvertedByteArray += "tcpobex://";
		                            break;
		                        case 0x1C:
		                        	ConvertedByteArray += "irdaobex://";
		                            break;
		                        case 0x1D:
		                        	ConvertedByteArray += "file://";
		                            break;
		                        case 0x1E:
		                        	ConvertedByteArray += "urn:epc:id:";
		                            break;
		                        case 0x1F:
		                        	ConvertedByteArray += "urn:epc:tag:";
		                            break;
		                        case 0x20:
		                        	ConvertedByteArray += "urn:epc:pat:";
		                            break;
		                        case 0x21:
		                        	ConvertedByteArray += "urn:epc:raw";
		                            break;
		                        case 0x22:
		                        	ConvertedByteArray += "urn:epc:";
		                            break;
		                        case 0x23:
		                        	ConvertedByteArray += "urn:nfc:";
		                            break;
		                        default:
		                        	ConvertedByteArray += "";
		                        	break;
	                        }
		                
		                    //NDEF Uri
		                    for(int i=0; i<intNDEFPayloadLength-1; i++)
		                    {
		                    	intByteArrayCounter++;
		    		            intLField--;
		    		            ConvertedByteArray += (char)ByteArrayToConvert[intByteArrayCounter];	    		            		    		            
		                    }		                
		                }
		                
		                else if (intNDEFRecordType == 0x5370) //Const RTD_SMART_POSTER = "5370" ("Sp")
		                {
		                	ConvertedByteArray += "type:SMARTPOSTER ";
		                }

		                else if (intNDEFRecordType == 0x6163) //Const RTD_ALTERNATIVE_CARRIER = "6163" ("ac")
		                {
		                	ConvertedByteArray += "type:ALTERNATIVE_CARRIER ";
		                }

		                else if (intNDEFRecordType == 0x4863) //Const RTD_HANDOVER_CARRIER = "4863" ("Hc")
		                {
		                	ConvertedByteArray += "type:HANDOVER_CARRIER ";
		                }
		                
		                else if (intNDEFRecordType == 0x4872) //Const RTD_HANDOVER_REQUEST = "4872" ("Hr")
		                {
		                	ConvertedByteArray += "type:HANDOVER_REQUEST ";
		                }

		                else if (intNDEFRecordType == 0x4873) //Const RTD_HANDOVER_SELECT = "4873" ("Hs")
		                {
		                	ConvertedByteArray += "type:HANDOVER_SELECT ";
		                }

		                else if (intNDEFRecordType == 0x616374) //Const RTD_ACT = "616374" ("act")
		                {
		                	ConvertedByteArray += "type:ACT " + (char)0x0D + (char)0x0A;
		                	//URI identifier
		                	int intNDEFUriIdentifier;
		                	intByteArrayCounter++;
			            	intLField--;
	                        intNDEFUriIdentifier = (int)(ByteArrayToConvert[intByteArrayCounter] & 0xFF);
	                        if (intNDEFUriIdentifier == 0x00)      //Do the action (Launch Browser / send sms / make the telphone call ...)
	                        	ConvertedByteArray += "   - do the action"  + (char)0x0D + (char)0x0A;
	                        else if (intNDEFUriIdentifier == 0x01)		//Save action for later (copy uri in url editor / save sms / save telephone number)
	                        	ConvertedByteArray += "   - save the action for later"  + (char)0x0D + (char)0x0A;
                            else if (intNDEFUriIdentifier == 0x02) 		//Open for editing (put uri in bookmark / open sms in editor / compose numero ... mais appelle pas)
                            	ConvertedByteArray += "   - open the action"  + (char)0x0D + (char)0x0A;
                            else                                   		//NFC forum = "RFU" can act as 0 or do nothing
                            	ConvertedByteArray += "   - action not understood"  + (char)0x0D + (char)0x0A;
		                }
		                
		                else 
		                {
		                	ConvertedByteArray += " " + (char)0x0D + (char)0x0A;
		                }
		            }
		            
		            //ElseIf (iStructureRecord = TNF_MIME_MEDIA) Then 'NFC forum MIME supported message
		            else if (bytStructureRecord == 0x02)
		            {
		            	//Type Length
		            	int intNDEFtypeLength;
		            	intByteArrayCounter++;
			            intLField--;
			            intNDEFtypeLength = (int)(ByteArrayToConvert[intByteArrayCounter] & 0xFF);		            
			            
			            //Payload Length
			            int intNDEFPayloadLength;	            	
		            	if (boolPayloadInOneByte == true)
		            	{	            	
		            		intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength = ByteArrayToConvert[intByteArrayCounter];
		            	}
		            	else
		            	{
		            		intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength = ByteArrayToConvert[intByteArrayCounter];
			            	intNDEFPayloadLength = intNDEFPayloadLength << 8;
			            	intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength += ByteArrayToConvert[intByteArrayCounter];
			            	intNDEFPayloadLength = intNDEFPayloadLength << 8;
			            	intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength += ByteArrayToConvert[intByteArrayCounter];
			            	intNDEFPayloadLength = intNDEFPayloadLength << 8;
			            	intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength += ByteArrayToConvert[intByteArrayCounter];
		            	}

		                //Record Type
		                String strNDEFRecordType = "";
		                for(int i=0; i<intNDEFtypeLength; i++)
		                {
		            		intByteArrayCounter++;
			            	intLField--;
			            	strNDEFRecordType += (char)ByteArrayToConvert[intByteArrayCounter];            
		                }
		                
		                
		                //Const MIME_TEXT_X_VCARD = "text/x-vCard"
		                if (strNDEFRecordType.equals ("text/x-vCard")) 
		                {
					         
		            		String strMessage = "";
	                        for(int i=0; i<11; i++) 	//11=length of BEGIN:VCARD
	                        {
	    	            		intByteArrayCounter++;
	    		            	intLField--;
	                            strMessage += (char)ByteArrayToConvert[intByteArrayCounter];
	                        }                        
	                        if (strMessage.equals ("BEGIN:VCARD"))
	                        {   
						         
	                        	ConvertedByteArray += "type:MIME:" + strNDEFRecordType + (char)0x0D + (char)0x0A + (char)0x0D + (char)0x0A;
                            	intByteArrayCounter++;
                            	intLField--;
                            	String strMessageTemp = "";
                            	boolean boolNDEFmessage=false;
	                        	while((ByteArrayToConvert[intByteArrayCounter] != (byte)0xFE) && intLField>0)
	                        	{	                        		
		                        	/*//String strMessageTemp = "";
		                        	if (ByteArrayToConvert[intByteArrayCounter] != (byte)0x0D && ByteArrayToConvert[intByteArrayCounter+1] != (byte)0x0A)
		    		            	{
		                        		if (ByteArrayToConvert[intByteArrayCounter] == (byte)0x3B)	//;
		                        			strMessageTemp += " ";
		                        		else
		                        			strMessageTemp += (char)ByteArrayToConvert[intByteArrayCounter];
		                        		boolNDEFmessage=true;
		    		            	}*/
	                        		//String strMessageTemp = "";
		                        	if (ByteArrayToConvert[intByteArrayCounter] != (byte)0x0D && ByteArrayToConvert[intByteArrayCounter+1] != (byte)0x0A)
		    		            	{
		                        		//if (ByteArrayToConvert[intByteArrayCounter] == (byte)0x3B)	//;
		                        		//	strMessageTemp += " ";
		                        		//else
		                        		//	strMessageTemp += (char)ByteArrayToConvert[intByteArrayCounter];
		                        		
		                        		strMessageTemp += (char)ByteArrayToConvert[intByteArrayCounter];
		                        		boolNDEFmessage=true;
		    		            	}
		                        	else if (ByteArrayToConvert[intByteArrayCounter-7] == (byte)0x3D && 
		                        			 ByteArrayToConvert[intByteArrayCounter-6] == (byte)0x30 &&
		                        			 ByteArrayToConvert[intByteArrayCounter-5] == (byte)0x44 &&
		                        			 ByteArrayToConvert[intByteArrayCounter-4] == (byte)0x3D &&
		                        			 ByteArrayToConvert[intByteArrayCounter-3] == (byte)0x30 &&
		                        			 ByteArrayToConvert[intByteArrayCounter-2] == (byte)0x41 &&
		                        			 ByteArrayToConvert[intByteArrayCounter-1] == (byte)0x3D)
		    		            	{
		                        		strMessageTemp += (byte)0x0D + (byte)0x0A;
		    		            	}
		                        	else
		                        	{
								         
		                        		int indexStart=0;
		                        		int indexEnd=0;
		                        		boolean boolTextFound=true;
		                        		if (boolNDEFmessage)
		                        		{                					        	                        		
		                        			//if (strMessageTemp.startsWith("N"))		
		                        			//	ConvertedByteArray += "NAME : ";
		                        			//else if (strMessageTemp.startsWith("FN"))
		                        			//	ConvertedByteArray += "FORMATED NAME : ";
		                        			//else if (strMessageTemp.startsWith("ORG"))
		                        			//	ConvertedByteArray += "ORGANISATION : ";		                        			
		                        			//else if (strMessageTemp.startsWith("TITLE"))
		                        			//	ConvertedByteArray += "TITLE : ";
		                        			//else if (strMessageTemp.startsWith("TEL"))
		                        			//{
		                        			//	if (strMessageTemp.contentEquals("FAX"))  
		                        			//			ConvertedByteArray += "FAX : ";
		                        			//	else  
		                        			//			ConvertedByteArray += "TEL : ";
		                        			//}
		                        			//else if (strMessageTemp.startsWith("ADR"))
		                        			//	ConvertedByteArray += "ADDRESS :";
		                        			//else if (strMessageTemp.startsWith("LABEL"))
		                        			//	ConvertedByteArray += "LABEL :";		                        			
		                        			//else if (strMessageTemp.startsWith("URL"))
		                        			//	ConvertedByteArray += "URL : ";
		                        			//else if (strMessageTemp.startsWith("EMAIL"))
		                        			//	ConvertedByteArray += "EMAIL : ";
		                        			//else
		                        			//	boolTextFound=false;

		                        			if (strMessageTemp.startsWith("N:") || strMessageTemp.startsWith("N;"))		
		                        				ConvertedByteArray += "NAME : ";
		                        			else if (strMessageTemp.startsWith("FN:") || strMessageTemp.startsWith("FN;"))
		                        				ConvertedByteArray += "FORMATED NAME : ";
		                        			else if (strMessageTemp.startsWith("ORG:") || strMessageTemp.startsWith("ORG;"))
		                        				ConvertedByteArray += "ORGANISATION : ";		                        			
		                        			else if (strMessageTemp.startsWith("TITLE:") || strMessageTemp.startsWith("TITLE;"))
		                        				ConvertedByteArray += "TITLE : ";
		                        			else if (strMessageTemp.startsWith("TEL:") || strMessageTemp.startsWith("TEL;"))
	                        				{
		                        				if (strMessageTemp.contentEquals("FAX:") || strMessageTemp.contentEquals("FAX;"))  
		                        						ConvertedByteArray += "FAX : ";
		                        				else  
		                        						ConvertedByteArray += "TEL : ";
	                        				}
		                        			else if (strMessageTemp.startsWith("ADR:") || strMessageTemp.startsWith("ADR;"))
		                        				ConvertedByteArray += "ADDRESS :";
		                        			else if (strMessageTemp.startsWith("LABEL:") || strMessageTemp.startsWith("LABEL;"))
		                        				ConvertedByteArray += "LABEL :";		                        			
		                        			else if (strMessageTemp.startsWith("URL:") || strMessageTemp.startsWith("URL;"))
		                        				ConvertedByteArray += "URL : ";
		                        			else if (strMessageTemp.startsWith("EMAIL:") || strMessageTemp.startsWith("EMAIL;"))
		                        				ConvertedByteArray += "EMAIL : ";
		                        			else
		                        				boolTextFound=false;
		                        			
		                        			if (boolTextFound==true)
		                        			{
		                        				for(int i=1; i<strMessageTemp.length(); i++)
		                        				{
		                        					if (strMessageTemp.charAt(i) == (char)0x3A) // 0x3A = : 
		                        					{
		                        						indexStart = i+1;
		                        						i = strMessageTemp.length() + 100; //rajout15/05/2012
		                        					}
		                        				}
		                        				String strMessageTempTemp = strMessageTemp.substring(indexStart, strMessageTemp.length());
		                        				ConvertedByteArray += strMessageTempTemp.replace("=0D=0A=", "")  + (char)0x0D + (char)0x0A;
		                        			}
	                        				strMessageTemp = "";
	                        				boolNDEFmessage = false;
	                        				boolTextFound=true;
	                        				//intLField--;
		                        			//intByteArrayCounter++;
		                        		}
		                        		intByteArrayCounter++;
		                        		intLField--;
		                        	}	
	                            	intByteArrayCounter++;
		                        	intLField--;
	                        	}
	                        	
	                        	//End of While loop due to FE found (or intLField<0 but not yet managed)	
						        boolLastTLVblock=true;	                        		                        	
	                        }
	                        
		                }
		                else if (strNDEFRecordType.startsWith("application/"))
		                {
		                	ConvertedByteArray += "type:MIME:" + strNDEFRecordType + (char)0x0D + (char)0x0A + (char)0x0D + (char)0x0A;
		                	ConvertedByteArray += strNDEFRecordType + (char)0x0D + (char)0x0A; 
		                    for(int i=0; i<intNDEFPayloadLength; i++)
		                    {
		                    	intByteArrayCounter++;
		    		            intLField--;
		    		            ConvertedByteArray += (char)ByteArrayToConvert[intByteArrayCounter];	    		            		    		            
		                    }			        
		                    //End of While loop due to FE found (or intLField<0 but not yet managed)	
					        boolLastTLVblock=true;
		                }
		                else if (strNDEFRecordType.startsWith("text/"))
		                {
		                	ConvertedByteArray += "type:MIME:" + strNDEFRecordType + (char)0x0D + (char)0x0A + (char)0x0D + (char)0x0A;
		                	ConvertedByteArray += strNDEFRecordType + (char)0x0D + (char)0x0A;
		                    for(int i=0; i<intNDEFPayloadLength; i++)
		                    {
		                    	intByteArrayCounter++;
		    		            intLField--;
		    		            ConvertedByteArray += (char)ByteArrayToConvert[intByteArrayCounter];	    		            		    		            
		                    }			          
		                    //End of While loop due to FE found (or intLField<0 but not yet managed)	
					        boolLastTLVblock=true;
		                }		                
		                else if (strNDEFRecordType.startsWith("image/"))
		                {
		                	ConvertedByteArray += "type:MIME:" + strNDEFRecordType + (char)0x0D + (char)0x0A + (char)0x0D + (char)0x0A;
		                	ConvertedByteArray += strNDEFRecordType + (char)0x0D + (char)0x0A; 
		                    for(int i=0; i<intNDEFPayloadLength; i++)
		                    {
		                    	intByteArrayCounter++;
		    		            intLField--;
		    		            ConvertedByteArray += (char)ByteArrayToConvert[intByteArrayCounter];	    		            		    		            
		                    }			          
		                    //End of While loop due to FE found (or intLField<0 but not yet managed)	
					        boolLastTLVblock=true;
		                }
		                else if (strNDEFRecordType.startsWith("audio/"))
		                {
		                	ConvertedByteArray += "type:MIME:" + strNDEFRecordType + (char)0x0D + (char)0x0A + (char)0x0D + (char)0x0A;
		                	ConvertedByteArray += strNDEFRecordType + (char)0x0D + (char)0x0A; 		                	
		                    for(int i=0; i<intNDEFPayloadLength; i++)
		                    {
		                    	intByteArrayCounter++;
		    		            intLField--;
		    		            ConvertedByteArray += (char)ByteArrayToConvert[intByteArrayCounter];	    		            		    		            
		                    }			          
		                    //End of While loop due to FE found (or intLField<0 but not yet managed)	
					        boolLastTLVblock=true;
		                }
		                else if (strNDEFRecordType.startsWith("video/"))
		                {
		                	ConvertedByteArray += "type:MIME:" + strNDEFRecordType + (char)0x0D + (char)0x0A + (char)0x0D + (char)0x0A;
		                	ConvertedByteArray += strNDEFRecordType + (char)0x0D + (char)0x0A; 
		                    for(int i=0; i<intNDEFPayloadLength; i++)
		                    {
		                    	intByteArrayCounter++;
		    		            intLField--;
		    		            ConvertedByteArray += (char)ByteArrayToConvert[intByteArrayCounter];	    		            		    		            
		                    }			          
		                    //End of While loop due to FE found (or intLField<0 but not yet managed)	
					        boolLastTLVblock=true;
		                }
		                else 
		                {
		                	ConvertedByteArray += "type:MIME:" + strNDEFRecordType + (char)0x0D + (char)0x0A + (char)0x0D + (char)0x0A;
		                	ConvertedByteArray += strNDEFRecordType + (char)0x0D + (char)0x0A;
		                    for(int i=0; i<intNDEFPayloadLength; i++)
		                    {
		                    	intByteArrayCounter++;
		    		            intLField--;
		    		            ConvertedByteArray += (char)ByteArrayToConvert[intByteArrayCounter];	    		            		    		            
		                    }
		                    //End of While loop due to FE found (or intLField<0 but not yet managed)	
					        boolLastTLVblock=true;
		                }
		            }
		          //ElseIf (iStructureRecord = TNF_EXTERNAL_TYPE) Then 'NFC forum MIME supported message
		            else if (bytStructureRecord == 0x04)
		            {
		            	//Type Length
		            	int intNDEFtypeLength;
		            	intByteArrayCounter++;
			            intLField--;
			            intNDEFtypeLength = (int)(ByteArrayToConvert[intByteArrayCounter] & 0xFF);		            
			            
			            //Payload Length
			            int intNDEFPayloadLength;	            	
		            	if (boolPayloadInOneByte == true)
		            	{	            	
		            		intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength = ByteArrayToConvert[intByteArrayCounter];
		            	}
		            	else
		            	{
		            		intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength = ByteArrayToConvert[intByteArrayCounter];
			            	intNDEFPayloadLength = intNDEFPayloadLength << 8;
			            	intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength += ByteArrayToConvert[intByteArrayCounter];
			            	intNDEFPayloadLength = intNDEFPayloadLength << 8;
			            	intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength += ByteArrayToConvert[intByteArrayCounter];
			            	intNDEFPayloadLength = intNDEFPayloadLength << 8;
			            	intByteArrayCounter++;
			            	intLField--;
			            	intNDEFPayloadLength += ByteArrayToConvert[intByteArrayCounter];
		            	}

		                //Record Type
		                String strNDEFRecordType = "";
		                for(int i=0; i<intNDEFtypeLength; i++)
		                {
		            		intByteArrayCounter++;
			            	intLField--;
			            	strNDEFRecordType += (char)ByteArrayToConvert[intByteArrayCounter];            
		                }
		                ConvertedByteArray += "type:EXTERNAL:" + strNDEFRecordType + (char)0x0D + (char)0x0A + (char)0x0D + (char)0x0A;
		                
		                //NDEF text
                        for(int i=0; i<intNDEFPayloadLength-3; i++)
                        {
    	            		intByteArrayCounter++;
    		            	intLField--;
    		            	ConvertedByteArray += (char)ByteArrayToConvert[intByteArrayCounter];
                        }
                        
		            }
		            else
		            {
		            	boolLastTLVblock = true;
		            }
		                		                		                
		            inttimeout++;
		            ConvertedByteArray += "" + (char)0x0D + (char)0x0A + "";
		            ConvertedByteArray += "" + (char)0x0D + (char)0x0A + "";
		            
		            if (ByteArrayToConvert[intByteArrayCounter+1] == (byte)0xFE)
		            	boolLastTLVblock=true;		            
			         
				}
		        while (intLField>0 && inttimeout<5 && boolLastTLVblock==false); //boolLastTLVblock=0xFE : "Last TLV block in the data area
		         
			}		
			//TLV
			else if(ByteArrayToConvert[4]==(byte)0xFE)
			{
				inttimeout=0; //rien de prévu pour l'instant
				ConvertedByteArray = "No NDEF message";
			}						
			else 
			{
				ConvertedByteArray = "No NDEF Message";
			}
			
			return ConvertedByteArray;

		}
						
}
