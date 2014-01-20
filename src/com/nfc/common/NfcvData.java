package com.nfc.common;

import android.nfc.Tag;
import android.util.Log;




public class NfcvData {

	private Tag currentTag;
	private String uid;
	private String techno;
	private String manufacturer;
	private String productName;
	private String dsfid;
	private String afi;
	private String memorySize;
	private String blockSize;
	private String icReference;
	private boolean basedOnTwoBytesAddress;
	private boolean MultipleReadSupported;
	private boolean MemoryExceed2048bytesSize;
	private byte[] uidRaw = new byte[8];
	private static final String TAG = "B4A";
	public NfcvData() {

	}
		

	byte[] ReadMultipleBlockAnswer = null;
	byte[] GetSystemInfoAnswer = null;
	int nbblocks = 0;
	
	//private byte [] addressStart = null;
	//private byte[] dataToWrite = new byte[4];
	private byte[] WriteSingleBlockAnswer = null;
	private long cpt = 0;
	

	
	
	public byte [] getReadData(){
		return ReadMultipleBlockAnswer;
	}
	
	
	public void readTag(byte [] addressStart,byte [] numberOfBlockToRead){
		
	  			
	  
  	  ReadMultipleBlockAnswer = null;
  	  long cpt = 0; 
  	  
  	  //if(DecodeGetSystemInfoResponse(GetSystemInfoAnswer))
		 // {	 					

		if(isMultipleReadSupported() == false || Helper.Convert2bytesHexaFormatToInt(numberOfBlockToRead) <=1) //ex: LRIS2K
		{
			while((ReadMultipleBlockAnswer == null || ReadMultipleBlockAnswer[0] == 1) && cpt <= 10)
			{
				//Used for DEBUG : 
				Log.i("ScanRead", "Dans le several read single block le cpt est ?-----> " + String.valueOf(cpt));
				ReadMultipleBlockAnswer = NFCCommand.Send_several_ReadSingleBlockCommands_NbBlocks(getCurrentTag(),addressStart,numberOfBlockToRead, basedOnTwoBytesAddress);
				cpt ++;
			}
			cpt = 0;
		}
		else if(Helper.Convert2bytesHexaFormatToInt(numberOfBlockToRead) <32)
		{
			while((ReadMultipleBlockAnswer == null || ReadMultipleBlockAnswer[0] == 1) && cpt <= 10)
			{
				//Used for DEBUG : 
				Log.i("ScanRead", "Dan le read MULTIPLE 1 le cpt est ?-----> " + String.valueOf(cpt));
				ReadMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom(getCurrentTag(),addressStart,numberOfBlockToRead[1], basedOnTwoBytesAddress);
				cpt ++;
			}
			cpt = 0;
		}
		else
		{
			while ((ReadMultipleBlockAnswer == null || ReadMultipleBlockAnswer[0] == 1) && cpt <= 10)
			{
				//Used for DEBUG : 
				Log.i("ScanRead", "Dans le read MULTIPLE 2 le cpt est ?-----> " + String.valueOf(cpt));
				ReadMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom2(getCurrentTag(),addressStart,numberOfBlockToRead, basedOnTwoBytesAddress,memorySize);
				cpt ++;
			}
			cpt = 0;
		}
				
		  //}		
		
		
	}
	
	public byte[]  writeData(byte [] addressStart , byte[] dataToWrite )
	{
		// TODO Auto-generated method stub
		cpt = 0;
		
		WriteSingleBlockAnswer = null;
		//if(DecodeGetSystemInfoResponse(GetSystemInfoAnswer))
    	//{
			while ((WriteSingleBlockAnswer == null || WriteSingleBlockAnswer[0] == 1) && cpt <= 10)
			{
				WriteSingleBlockAnswer = NFCCommand.SendWriteSingleBlockCommand(getCurrentTag(), addressStart, dataToWrite, basedOnTwoBytesAddress);
				Log.i(TAG, "WriteSingBloackAnser inside writeData is " + Helper.ConvertHexByteArrayToString(WriteSingleBlockAnswer));

				cpt++;
			}
    	//}
		return 	WriteSingleBlockAnswer;
	}

	
	 //***********************************************************************/
	 //* the function Decode the tag answer for the GetSystemInfo command
	 //* the function fills the values (dsfid / afi / memory size / icRef /..) 
	 //* in the myApplication class. return true if everything is ok.
	 //***********************************************************************/
	 public boolean DecodeGetSystemInfoResponse (byte[] GetSystemInfoResponse)
	 {
		 //Log.i(TAG, "Decoding GetSystemInfoResponse");
		 //if the tag has returned a good response
		 if(GetSystemInfoResponse[0] == (byte) 0x00 && GetSystemInfoResponse.length >= 12)
		 { 
			 //Log.i(TAG, "Good GetSystemInfoReponse");
			 //assert ma != null;
			 String uidToString = "";
			 
			 // change uid format from byteArray to a String
			 for (int i = 1; i <= 8; i++) 
			 {
				 uidRaw[i - 1] = GetSystemInfoResponse[10 - i];
				 uidToString += Helper.ConvertHexByteToString(uidRaw[i - 1]);
			 }			 

			 //***** TECHNO ******
			 setUid(uidToString);
			 if(uidRaw[0] == (byte) 0xE0)
			 		 setTechno("ISO 15693");
			 else if (uidRaw[0] == (byte) 0xD0)
			 	 setTechno("ISO 14443");
			 else
			 	 setTechno("Unknown techno");			 
			 			
			 //***** MANUFACTURER ****
			 if(uidRaw[1]== (byte) 0x02)
			 	 setManufacturer("STMicroelectronics");
			 else if(uidRaw[1]== (byte) 0x04)
			 	 setManufacturer("NXP");
			 else if(uidRaw[1]== (byte) 0x07)
			 	 setManufacturer("Texas Instrument");
			 else
			 	 setManufacturer("Unknown manufacturer");						 			
			 			 
			 //**** PRODUCT NAME *****
			 if(uidRaw[2] >= (byte) 0x04 && uidRaw[2] <= (byte) 0x07)
			 {
			 	 setProductName("LRI512");
			 	 setMultipleReadSupported(false);
			 	 setMemoryExceed2048bytesSize(false);
			 }
			 else if(uidRaw[2] >= (byte) 0x14 && uidRaw[2] <= (byte) 0x17)
			 {
			 	 setProductName("LRI64");
			 	 setMultipleReadSupported(false);
			 	 setMemoryExceed2048bytesSize(false);
			 }
			 else if(uidRaw[2] >= (byte) 0x20 && uidRaw[2] <= (byte) 0x23)
			 {
			 	 setProductName("LRI2K");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(false);
			 }
			 else if(uidRaw[2] >= (byte) 0x28 && uidRaw[2] <= (byte) 0x2B)
			 {
			 	 setProductName("LRIS2K");
			 	 setMultipleReadSupported(false);	
			 	 setMemoryExceed2048bytesSize(false);
			 }
			 else if(uidRaw[2] >= (byte) 0x2C && uidRaw[2] <= (byte) 0x2F)
			 {
			 	 setProductName("M24LR64");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(true);
			 }
			 else if(uidRaw[2] >= (byte) 0x40 && uidRaw[2] <= (byte) 0x43)
			 {
			 	 setProductName("LRI1K");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(false);
			 }
			 else if(uidRaw[2] >= (byte) 0x44 && uidRaw[2] <= (byte) 0x47)
			 {
			 	 setProductName("LRIS64K");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(true);
			 }
			 else if(uidRaw[2] >= (byte) 0x48 && uidRaw[2] <= (byte) 0x4B)
			 {
			 	 setProductName("M24LR01E");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(false);
			 }
			 else if(uidRaw[2] >= (byte) 0x4C && uidRaw[2] <= (byte) 0x4F)
			 {
			 	 setProductName("M24LR16E");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(true);
			 	 if(isBasedOnTwoBytesAddress() == false)
				 	return false;
			 }
			 else if(uidRaw[2] >= (byte) 0x50 && uidRaw[2] <= (byte) 0x53)
			 {
			 	 setProductName("M24LR02E");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(false);
			 }
			 else if(uidRaw[2] >= (byte) 0x54 && uidRaw[2] <= (byte) 0x57)
			 {
			 	 setProductName("M24LR32E");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(true);
			 	 if(isBasedOnTwoBytesAddress() == false)
				 	return false;
			 }
			 else if(uidRaw[2] >= (byte) 0x58 && uidRaw[2] <= (byte) 0x5B)
			 {
				 setProductName("M24LR04E");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(true);
			 }
			 else if(uidRaw[2] >= (byte) 0x5C && uidRaw[2] <= (byte) 0x5F)
			 {
			 	 setProductName("M24LR64E");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(true);
			 	 if(isBasedOnTwoBytesAddress() == false)
				 	return false;
			 }
			 else if(uidRaw[2] >= (byte) 0x60 && uidRaw[2] <= (byte) 0x63)
			 {
			 	 setProductName("M24LR08E");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(true);
			 }
			 else if(uidRaw[2] >= (byte) 0x64 && uidRaw[2] <= (byte) 0x67)
			 {
			 	 setProductName("M24LR128E");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(true);
			 	 if(isBasedOnTwoBytesAddress() == false)
				 	return false;
			 }
			 else if(uidRaw[2] >= (byte) 0x6C && uidRaw[2] <= (byte) 0x6F)
			 {
			 	 setProductName("M24LR256E");
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(true);
			 	 if(isBasedOnTwoBytesAddress() == false)
				 	return false;
			 }
			 else if(uidRaw[2] >= (byte) 0xF8 && uidRaw[2] <= (byte) 0xFB)
			 {
			 	 setProductName("detected product");
			 	 setBasedOnTwoBytesAddress(true);
			 	 setMultipleReadSupported(true);
			 	 setMemoryExceed2048bytesSize(true);
			 }	 
			 else
			 {
			 	 setProductName("Unknown product");
			 	 setBasedOnTwoBytesAddress(false);
			 	 setMultipleReadSupported(false);
			 	 setMemoryExceed2048bytesSize(false);
			 }
			 
			 //*** DSFID ***
			 setDsfid(Helper.ConvertHexByteToString(GetSystemInfoResponse[10]));
			 
			//*** AFI ***
			 setAfi(Helper.ConvertHexByteToString(GetSystemInfoResponse[11]));			 
			 
			//*** MEMORY SIZE ***
			 if(isBasedOnTwoBytesAddress())
			 {
				 String temp = new String();
				 temp += Helper.ConvertHexByteToString(GetSystemInfoResponse[13]);
				 temp += Helper.ConvertHexByteToString(GetSystemInfoResponse[12]);
				 setMemorySize(temp);
			 }
			 else 
				 setMemorySize(Helper.ConvertHexByteToString(GetSystemInfoResponse[12]));
			 
			//*** BLOCK SIZE ***
			 if(isBasedOnTwoBytesAddress())
				 setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));
			 else
				 setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[13]));

			//*** IC REFERENCE ***
			 if(isBasedOnTwoBytesAddress())
				 setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[15]));
			 else
				 setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));
			 //Log.i(TAG, "return true in Decoding GetSystemInfo");	 
			 return true;
		 }
		 
		//if the tag has returned an error code 
		 else{
			 Log.i(TAG, "return false in Decoding GetSystemInfo");
			 return false;
		 }
	 }	
	
/*	 public void setCurrentTag(Tag tag){
		 ma.setCurrentTag(tag);
	 }

	 public Tag getCurrentTag()
	 {
		 return ma.getCurrentTag(); 
	 }
	 
	 public String getMemorySize(){
		 return ma.getMemorySize();
	 }
	 */
	 
	 
	 public boolean getSystemInfo(){
		 GetSystemInfoAnswer = NFCCommand.SendGetSystemInfoCommandCustom(getCurrentTag(),this);		 
		 
		 
		 return DecodeGetSystemInfoResponse(GetSystemInfoAnswer);
	 }

	 
	 //From DataDevice
	 
	 

		public void setCurrentTag(Tag currentTag) {
			this.currentTag = currentTag;
		}

		 public byte[] getUidRaw(){
			 return uidRaw;
		 }

		
		public Tag getCurrentTag() {
			return currentTag;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getUid() {
			return uid;
		}

		public void setTechno(String techno) {
			this.techno = techno;
		}

		public String getTechno() {
			return techno;
		}

		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		public String getManufacturer() {
			return manufacturer;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getProductName() {
			return productName;
		}

		public void setDsfid(String dsfid) {
			this.dsfid = dsfid;
		}

		public String getDsfid() {
			return dsfid;
		}

		public void setAfi(String afi) {
			this.afi = afi;
		}

		public String getAfi() {
			return afi;
		}

		public void setMemorySize(String memorySize) {
			this.memorySize = memorySize;
		}

		public String getMemorySize() {
			return memorySize;
		}

		public void setBlockSize(String blockSize) {
			this.blockSize = blockSize;
		}

		public String getBlockSize() {
			return blockSize;
		}

		public void setIcReference(String icReference) {
			this.icReference = icReference;
		}

		public String getIcReference() {
			return icReference;
		}

		public void setBasedOnTwoBytesAddress(boolean basedOnTwoBytesAddress) {
			this.basedOnTwoBytesAddress = basedOnTwoBytesAddress;
		}

		public boolean isBasedOnTwoBytesAddress() {
			return basedOnTwoBytesAddress;
		}

		public void setMultipleReadSupported(boolean MultipleReadSupported) {
			this.MultipleReadSupported = MultipleReadSupported;
		}

		public boolean isMultipleReadSupported() {
			return MultipleReadSupported;
		}	
		
		public void setMemoryExceed2048bytesSize(boolean MemoryExceed2048bytesSize) {
			this.MemoryExceed2048bytesSize = MemoryExceed2048bytesSize;
		}

		public boolean isMemoryExceed2048bytesSize() {
			return MemoryExceed2048bytesSize;
		}	
			 
}
