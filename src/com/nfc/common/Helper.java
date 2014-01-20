// THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS 
// WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE 
// TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY 
// DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS 
// ARISING FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS 
// OF THE CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.

package com.nfc.common;



//**** SUMMARY *****

// castHexKeyboard
// input : String "AZER" 
// output : String "AFEF"

// StringForceDigit
// input : String "23"
// input : Int 4
// output : String"0023"

// ConvertHexByteToString 
// input :  byte 0x0F  
// output : String "0F" 

// ConvertHexByteArrayToStrin
// input :  byte[] { 0X0F ; 0X43 ; 0xA4 ; ...}
// output : String "0F 43 A4 ..." 

// FormatStringAddressStart
// input : String "0F"
// input : DataDevice
// output: String  "000F"

// ConvertIntToHexFormatString
// input : Int 2047
// output : String "7FF"

// FormatStringNbBlock
// input :  String "2"  
// output : String "02"

// ConvertStringToHexBytes
// input : String "0F43" 
// output : byte[] { 0X0F ; 0X43 }

// ConvertStringToHexByte
// input : String "43" 
// output : byte { 0X43 }

// ConvertIntTo2bytesHexaFormat
// input : Int 1876 
// output : byte[] {0x07, 0x54}

// Convert2bytesHexaFormatToInt
// input : byte[] {0x07, 0x54}
// output : Int 1876

// ConvertStringToInt
// input : String "0754"
// output : Int 1876

// FormatDisplayReadBlock 
// input : byte[] ReadMultipleBlockAnswer & byte[]AddressStart 
// output : String "Block 0 : 32 FF EE 44"

public class Helper {

	
	//***********************************************************************/
	//* the function cast a String to hexa character only
	//* when a character is not hexa it's replaced by '0'
	//* Example : ConvertHexByteToString ("AZER") -> returns "AFEF"
	//* Example : ConvertHexByteToString ("12l./<4") -> returns "12FFFF4"
	//***********************************************************************/
	public static String castHexKeyboard (String sInput)
	{
		String sOutput ="";
		
		sInput = sInput.toUpperCase();
		char[] cInput = sInput.toCharArray();
		
		for(int i=0; i<sInput.length();i++)
		{
			if( cInput[i] != '0' && cInput[i] != '1' && cInput[i] != '2' && cInput[i] != '3' && cInput[i] != '4' && cInput[i] != '5' && 
				cInput[i] != '6' && cInput[i] != '7' && cInput[i] != '8' && cInput[i] != '9' && cInput[i] != 'A' && cInput[i] != 'B' && 
				cInput[i] != 'C' && cInput[i] != 'D' && cInput[i] != 'E')
			{
				cInput[i] = 'F';
			}
			sOutput += cInput[i];
		}
		
		return sOutput;
	}

	//***********************************************************************/
	//* the function cast a String to hexa character only
	//* when a character is not hexa it's replaced by '0'
	//* Example : ConvertHexByteToString ("AZER") -> returns "AFEF"
	//* Example : ConvertHexByteToString ("12l./<4") -> returns "12FFFF4"
	//***********************************************************************/
	public static boolean checkDataHexa (String sInput)
	{
		boolean checkedValue = true;
		sInput = sInput.toUpperCase();
		char[] cInput = sInput.toCharArray();
		
		for(int i=0; i<sInput.length();i++)
		{
			if( cInput[i] != '0' && cInput[i] != '1' && cInput[i] != '2' && cInput[i] != '3' && cInput[i] != '4' && cInput[i] != '5' && 
				cInput[i] != '6' && cInput[i] != '7' && cInput[i] != '8' && cInput[i] != '9' && cInput[i] != 'A' && cInput[i] != 'B' && 
				cInput[i] != 'C' && cInput[i] != 'D' && cInput[i] != 'E' && cInput[i] != 'F')
			{
				checkedValue=  false;
			}
		}
		return checkedValue;		
	}

	//***********************************************************************/
	//* the function cast a String to hexa character only
	//* when a character is not hexa it's replaced by '0'
	//* Example : ConvertHexByteToString ("AZER") -> returns "AFEF"
	//* Example : ConvertHexByteToString ("12l./<4") -> returns "12FFFF4"
	//***********************************************************************/
	public static String checkAndChangeDataHexa (String sInput)
	{
		String CheckedAndChangedValue = "";
		sInput = sInput.toUpperCase();
		char[] cInput = sInput.toCharArray();
		
		for(int i=0; i<sInput.length();i++)
		{
			if( cInput[i] == '0' || cInput[i] == '1' || cInput[i] == '2' || cInput[i] == '3' || 
				cInput[i] == '4' || cInput[i] == '5' ||	cInput[i] == '6' || cInput[i] == '7' || 
				cInput[i] == '8' || cInput[i] == '9' || cInput[i] == 'A' || cInput[i] == 'B' || 
				cInput[i] == 'C' || cInput[i] == 'D' || cInput[i] == 'E' || cInput[i] == 'F')
			{
				CheckedAndChangedValue +=  cInput[i];
			}
		}
		return CheckedAndChangedValue;		
	}
	
	//***********************************************************************/
	//* the function cast a String to hexa character only
	//* when a character is not hexa it's replaced by '0'
	//* Example : ConvertHexByteToString ("AZER") -> returns "AFEF"
	//* Example : ConvertHexByteToString ("12l./<4") -> returns "12FFFF4"
	//***********************************************************************/
	public static boolean checkFileName (String sInput)
	{
		boolean checkedValue = true;
		//sInput = sInput.toUpperCase();
		char[] cInput = sInput.toCharArray();
		
		for(int i=0; i<sInput.length();i++)
		{
			if( cInput[i] != '0' && cInput[i] != '1' && cInput[i] != '2' && cInput[i] != '3' && cInput[i] != '4' && 
					cInput[i] != '5' &&	cInput[i] != '6' && cInput[i] != '7' && cInput[i] != '8' && cInput[i] != '9' &&
					cInput[i] != 'a' && cInput[i] != 'b' && cInput[i] != 'c' && cInput[i] != 'd' && cInput[i] != 'e' && 
					cInput[i] != 'f' && cInput[i] != 'g' && cInput[i] != 'h' && cInput[i] != 'i' && cInput[i] != 'j' && 
					cInput[i] != 'k' && cInput[i] != 'l' && cInput[i] != 'm' && cInput[i] != 'n' && cInput[i] != 'o' && 
					cInput[i] != 'p' && cInput[i] != 'q' && cInput[i] != 'r' && cInput[i] != 's' && cInput[i] != 't' &&
					cInput[i] != 'u' && cInput[i] != 'v' && cInput[i] != 'w' && cInput[i] != 'x' && cInput[i] != 'y' && 
					cInput[i] != 'z' &&				
					cInput[i] != 'A' && cInput[i] != 'B' && cInput[i] != 'C' && cInput[i] != 'D' && cInput[i] != 'E' && 
					cInput[i] != 'F' && cInput[i] != 'G' && cInput[i] != 'H' && cInput[i] != 'I' && cInput[i] != 'J' && 
					cInput[i] != 'K' && cInput[i] != 'L' && cInput[i] != 'M' && cInput[i] != 'N' && cInput[i] != 'O' && 
					cInput[i] != 'P' && cInput[i] != 'Q' && cInput[i] != 'R' && cInput[i] != 'S' && cInput[i] != 'T' &&
					cInput[i] != 'U' && cInput[i] != 'V' && cInput[i] != 'W' && cInput[i] != 'X' && cInput[i] != 'Y' && 
					cInput[i] != 'Z' &&
					cInput[i] != '.' && cInput[i] != '_')
			{
				checkedValue=  false;
			}
		}
		return checkedValue;		
	}

	//***********************************************************************/
	//* the function cast a String to hexa character only
	//* when a character is not hexa it's replaced by '0'
	//* Example : ConvertHexByteToString ("AZER") -> returns "AFEF"
	//* Example : ConvertHexByteToString ("12l./<4") -> returns "12FFFF4"
	//***********************************************************************/
	public static String checkAndChangeFileName (String sInput)
	{
		String CheckedAndChangedName = "";
		//sInput = sInput.toUpperCase();
		char[] cInput = sInput.toCharArray();
		
		for(int i=0; i<sInput.length();i++)
		{
			if( cInput[i] == '0' || cInput[i] == '1' || cInput[i] == '2' || cInput[i] == '3' || cInput[i] == '4' || 
				cInput[i] == '5' ||	cInput[i] == '6' || cInput[i] == '7' || cInput[i] == '8' || cInput[i] == '9' ||
				cInput[i] == 'a' || cInput[i] == 'b' || cInput[i] == 'c' || cInput[i] == 'd' || cInput[i] == 'e' || 
				cInput[i] == 'f' || cInput[i] == 'g' || cInput[i] == 'h' || cInput[i] == 'i' || cInput[i] == 'j' || 
				cInput[i] == 'k' || cInput[i] == 'l' || cInput[i] == 'm' || cInput[i] == 'n' || cInput[i] == 'o' || 
				cInput[i] == 'p' || cInput[i] == 'q' || cInput[i] == 'r' || cInput[i] == 's' || cInput[i] == 't' ||
				cInput[i] == 'u' || cInput[i] == 'v' || cInput[i] == 'w' || cInput[i] == 'x' || cInput[i] == 'y' || 
				cInput[i] == 'z' ||				
				cInput[i] == 'A' || cInput[i] == 'B' || cInput[i] == 'C' || cInput[i] == 'D' || cInput[i] == 'E' || 
				cInput[i] == 'F' || cInput[i] == 'G' || cInput[i] == 'H' || cInput[i] == 'I' || cInput[i] == 'J' || 
				cInput[i] == 'K' || cInput[i] == 'L' || cInput[i] == 'M' || cInput[i] == 'N' || cInput[i] == 'O' || 
				cInput[i] == 'P' || cInput[i] == 'Q' || cInput[i] == 'R' || cInput[i] == 'S' || cInput[i] == 'T' ||
				cInput[i] == 'U' || cInput[i] == 'V' || cInput[i] == 'W' || cInput[i] == 'X' || cInput[i] == 'Y' || 
				cInput[i] == 'Z' ||
				cInput[i] == '.' || cInput[i] == '_')
			{
				CheckedAndChangedName +=  cInput[i];
			}
		}
		return CheckedAndChangedName;		
	}	
	
	//***********************************************************************/
	//* the function Format a String with the right number of digit
	//* Example : ConvertHexByteToString ("23",4) -> returns "0023"
	//* Example : ConvertHexByteToString ("54",7) -> returns "0000054"
	//***********************************************************************/
	public static String StringForceDigit (String sStringToFormat, int nbOfDigit)
	{
		String sStringFormated = sStringToFormat.replaceAll(" ", "");
		
		if(sStringFormated.length() == 4)
		{
			return sStringFormated;
		}
		else if (sStringFormated.length() < nbOfDigit)
		{
			while (sStringFormated.length() !=nbOfDigit)
			{
				sStringFormated = "0".concat(sStringFormated);
			}
		}
		
		return sStringFormated;
	}
	
	//***********************************************************************/
	 //* the function Convert byte value to a "2-char String" Format
	 //* Example : ConvertHexByteToString ((byte)0X0F) -> returns "0F"
	 //***********************************************************************/
	public static String ConvertHexByteToString (byte byteToConvert)
	 {
		 String ConvertedByte = "";
		 if (byteToConvert < 0) {
			 ConvertedByte += Integer.toString(byteToConvert + 256, 16)
					+ " ";
		} else if (byteToConvert <= 15) {
			ConvertedByte += "0" + Integer.toString(byteToConvert, 16)
					+ " ";
		} else {
			ConvertedByte += Integer.toString(byteToConvert, 16) + " ";
		}		
		 
		 return ConvertedByte;
	 }
	
	
	//***********************************************************************/
	 //* the function Convert byte Array to a "String" Formated with spaces
	 //* Example : ConvertHexByteArrayToString { 0X0F ; 0X43 } -> returns "0F 43"
	 //***********************************************************************/
	public static String ConvertHexByteArrayToString (byte[] byteArrayToConvert)
	 {
		 String ConvertedByte = "";
		 for(int i=0;i<byteArrayToConvert.length;i++)
		 {
			 if (byteArrayToConvert[i] < 0) {
				 ConvertedByte += Integer.toString(byteArrayToConvert[i] + 256, 16)
						+ " ";
			} else if (byteArrayToConvert[i] <= 15) {
				ConvertedByte += "0" + Integer.toString(byteArrayToConvert[i], 16)
						+ " ";
			} else {
				ConvertedByte += Integer.toString(byteArrayToConvert[i], 16) + " ";
			}		
		 }

		 return ConvertedByte;
	 }
	
	//***********************************************************************/
	 //* the function verify and convert the start address from the EditText
	 //* in order to not read out of memory range and code String on 4chars.
	 //* Example : FormatStringAddressStart ("0F") -> returns "000F"
	 //* Example : FormatStringAddressStart ("FFFF") -> returns "07FF"
	 //***********************************************************************/
	public static String FormatStringAddressStart (String stringToFormat, String strMemorySize)
	{
		String stringFormated = stringToFormat;
		stringFormated = StringForceDigit(stringToFormat,4);
		
		if (stringFormated.length() > 4)
		{
			stringFormated = strMemorySize.replace(" ", "");
		}
		
		int iAddressStart = ConvertStringToInt(stringFormated);
		int iAddresStartMax = ConvertStringToInt(StringForceDigit(strMemorySize,4));

		if(iAddressStart > iAddresStartMax)
		{
			iAddressStart = iAddresStartMax;
		}
			
		stringFormated = ConvertIntToHexFormatString(iAddressStart);
		
		
		return stringFormated.toUpperCase();
	}
	
	//***********************************************************************/
	//* this function give the right format for the 4 EditText to fill in
	//* the screen BASICWRITE
	//***********************************************************************/
	public static String FormatValueByteWrite (String stringToFormat)
	{
		String stringFormated = stringToFormat;
		stringFormated = StringForceDigit(stringToFormat,2);
		stringFormated = castHexKeyboard(stringFormated);
		return stringFormated.toUpperCase();
	}
	
	//***********************************************************************/
	//* the function convert an Int value to a String with Hexadecimal format
	//* Example : ConvertIntToHexFormatString (2047) -> returns "7FF"
	//***********************************************************************/
	public static String ConvertIntToHexFormatString (int iNumberToConvert)
	{
		String sConvertedNumber ="";
		byte[] bNumberToConvert;
		
		bNumberToConvert = ConvertIntTo2bytesHexaFormat(iNumberToConvert);
		sConvertedNumber = ConvertHexByteArrayToString(bNumberToConvert);
		sConvertedNumber = sConvertedNumber.replaceAll(" ","");
		return sConvertedNumber ;
	}
	

		//***********************************************************************/
		 //* the function verify and convert the NbBlock from the EditText (DECIMAL)
		 //* in order to not read out of memory range and code String on 4chars.
		 //* Example : FormatStringAddressStart ("01") -> returns "0001"
		 //* Example : FormatStringAddressStart ("9999") -> returns "2048"
		 //***********************************************************************/
		/*
		public static String FormatStringNbBlockInteger (String stringToformat, String sAddressStart,DataDevice ma)
		{
			String sNbBlockToRead = stringToformat;
			sNbBlockToRead = StringForceDigit(sNbBlockToRead,4);
			
			if (sNbBlockToRead.length() > 4)
			{
				sNbBlockToRead = ma.getMemorySize().replace(" ", "");
			}
			
			int iNbBlockToRead = Integer.parseInt(sNbBlockToRead);
			int iAddressStart = ConvertStringToInt(sAddressStart);
			int iAddresStartMax = ConvertStringToInt(StringForceDigit(ma.getMemorySize(),4));

			if(iAddressStart + iNbBlockToRead > iAddresStartMax + 1)
			{
				iNbBlockToRead = iAddresStartMax - iAddressStart +1;
			}

				
			sNbBlockToRead = Integer.toString(iNbBlockToRead, 10);
			sNbBlockToRead = StringForceDigit(sNbBlockToRead,4);
			
			return sNbBlockToRead;
		}*/
		
		//***********************************************************************/
		 //* the function duplicates above function but using String sMemorySize instead of DataDevice ma
		 //***********************************************************************/
		public static String FormatStringNbBlockInteger (String stringToformat, String sAddressStart,String sMemorySize )
		{
			String sNbBlockToRead = stringToformat;
			sNbBlockToRead = StringForceDigit(sNbBlockToRead,4);
			
			if (sNbBlockToRead.length() > 4)
			{
				sNbBlockToRead = sMemorySize.replace(" ", "");
			}
			
			int iNbBlockToRead = Integer.parseInt(sNbBlockToRead);
			int iAddressStart = ConvertStringToInt(sAddressStart);
			int iAddresStartMax = ConvertStringToInt(StringForceDigit(sMemorySize,4));

			if(iAddressStart + iNbBlockToRead > iAddresStartMax + 1)
			{
				iNbBlockToRead = iAddresStartMax - iAddressStart +1;
			}
			/*
			else if(iNbBlockToRead > iAddresStartMax)
			{
				iNbBlockToRead = iAddresStartMax +1;
			}
			*/
				
			sNbBlockToRead = Integer.toString(iNbBlockToRead, 10);
			sNbBlockToRead = StringForceDigit(sNbBlockToRead,4);
			
			return sNbBlockToRead;
		}
		

	//***********************************************************************/
	//* the function Convert a "4-char String" to a two bytes format
	//* Example : "0F43" -> { 0X0F ; 0X43 }
	//***********************************************************************/
	public static byte[] ConvertStringToHexBytes (String StringToConvert)
	 {
		StringToConvert = StringToConvert.toUpperCase(); 
		StringToConvert = StringToConvert.replaceAll(" ", "");
		char[]CharArray = StringToConvert.toCharArray();
		 char[] Char = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		 int result = 0;
		 byte[] ConvertedString = new byte[]{(byte)0x00,(byte)0x00};
		 for(int i=0;i <= 1 ; i++ )
		 {
			 
			 for(int j=0; j<=15 ; j++)
			 {
				 if(CharArray[i] == Char[j])
				 {
					 if(i == 1)
					 {
						 result = result + j;
						 j=15;
					 }
						 
					 else if (i==0)
					 {
						 result = result + j*16;
						 j = 15;
					 }
						 
				 }
			 }
		 }
		 ConvertedString[0]= (byte)result;
		 
		 result = 0;
		 for(int i=2;i <= 3 ; i++ )
		 {
			 for(int j=0; j<=15 ; j++)
			 {
				 if(CharArray[i] == Char[j])
				 {
					 if(i==3)
					 {
						 result = result + j;
						 j=15;
					 }
						 
					 else if (i==2)
					 {
						 result = result + j*16;
						 j=15;
					 }
						 
				 }
			 }
		 }
		 ConvertedString[1]= (byte)result;
		  
		 return ConvertedString;
	 }
	
	
	//***********************************************************************/
	//* the function Convert a "4-char String" to a X bytes format
	//* Example : "0F43" -> { 0X0F ; 0X43 }
	//***********************************************************************/
	public static byte[] ConvertStringToHexBytesArray (String StringToConvert)
	 {
			StringToConvert = StringToConvert.toUpperCase(); 
			StringToConvert = StringToConvert.replaceAll(" ", "");
			char[]CharArray = StringToConvert.toCharArray();
			char[] Char = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			int result = 0;
			byte[] ConvertedString = new byte[StringToConvert.length()/2];
			int iStringLen = (StringToConvert.length());
					
			for(int i=0;i <iStringLen ; i++ )
			{				 
				 for(int j=0; j<=15 ; j++)
				 {
					 if(CharArray[i] == Char[j])
					 {
						 if(i%2 == 1)
						 {
							 result = result + j;
							 j=15;
						 }
							 
						 else if (i%2 == 0)
						 {
							 result = result + j*16;
							 j = 15;
						 }
							 
					 }
				 }
				 if (i%2 == 1) 
				 {
					 ConvertedString[i/2]= (byte)result;
					 result = 0;
				 }
			}			
			  
			return ConvertedString;
	 }	
	
	//***********************************************************************/
	 //* the function Convert a "4-char String" to a two bytes format
	 //* Example : "43" -> { 0X43 }
	 //***********************************************************************/
	public static byte ConvertStringToHexByte (String StringToConvert)
	 {
		StringToConvert = StringToConvert.toUpperCase(); 
		char[]CharArray = StringToConvert.toCharArray();
		 char[] Char = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		 int result = 0;
		 for(int i=0;i <= 1 ; i++ )
		 { 
			 for(int j=0; j<=15 ; j++)
			 {
				 if(CharArray[i] == Char[j])
				 {
					 if(i == 1)
					 {
						 result = result + j;
						 j=15;
					 }
						 
					 else if (i==0)
					 {
						 result = result + j*16;
						 j = 15;
					 } 
				 }
			 }
		 }
		 return (byte)result;
	 } 
	
	
	//***********************************************************************/
	 //* the function Convert Int value to a "2 bytes Array" Format
	//*  (decimal)1876 == (hexadecimal)0754 
	 //* Example : ConvertIntTo2bytesHexaFormat (1876) -> returns {0x07, 0x54}
	 //***********************************************************************/
	public static byte[] ConvertIntTo2bytesHexaFormat (int numberToConvert)
	 {
		 byte[] ConvertedNumber = new byte[2];
		 
		 ConvertedNumber[0]= (byte)(numberToConvert/256);
		 ConvertedNumber[1]= (byte)(numberToConvert-(256*(numberToConvert/256)));
		 
		 return ConvertedNumber;
	 }
	
	//***********************************************************************/
	 //* the function Convert a "2 bytes Array" To int Format
	//*  (decimal)1876 = (hexadecimal)0754 
	 //* Example : Convert2bytesHexaFormatToInt {0x07, 0x54} -> returns 1876
	 //***********************************************************************/
	public static int Convert2bytesHexaFormatToInt (byte[] ArrayToConvert)
	 {
		 int ConvertedNumber = 0;
		 if(ArrayToConvert[1]<=-1)//<0
			 ConvertedNumber += ArrayToConvert[1]+256;
		 else
			 ConvertedNumber += ArrayToConvert[1];
		 
		 if(ArrayToConvert[0]<=-1)//<0
			 ConvertedNumber += (ArrayToConvert[0]*256)+256;
		 else
			 ConvertedNumber += ArrayToConvert[0]*256;
		 
		 return ConvertedNumber;
	 }
	
	//***********************************************************************/
	//* the function Convert String to an Int value
	//***********************************************************************/
	public static int ConvertStringToInt (String nbOfBlocks)
	{
		int count = 0;
		
		if(nbOfBlocks.length()>2)
		{
			String msb = nbOfBlocks.substring(0, 2);
			String lsb = nbOfBlocks.substring(2, 4);
		
			count = Integer.parseInt(lsb,16);
			count += (Integer.parseInt(msb,16))*256;
		}
		else
		{
			String lsb = nbOfBlocks.substring(0, 2);
			count = Integer.parseInt(lsb,16);
		}
		
		return count;
	}
	

	public static String[] buildArrayBlocks(byte[] addressStart, int length)
	{
		String array[] = new String[length];
		
		int add = (int)addressStart[1];
		
		if((int)addressStart[1]<0)
			add = ((int)addressStart[1]+256);
		
		if((int)addressStart[0]<0)
			add += (256*((int)addressStart[0]+256));
		else
			add += (256*(int)addressStart[0]);
		
		for (int i = 0; i < length; i++)
		{
			if(i == 14)
			{
				i =14;
			}
			array[i] = "Block  " + ConvertIntToHexFormatString(i + add).toUpperCase();
		}
		
		return array;
	}
	
	public static String[] buildArrayValueBlocks(byte[]ReadMultipleBlockAnswer, int length)
	{
		String array[] = new String[length];
		int sup = 1;
		
		for (int i = 0; i < length; i++)
		{
			array[i] = "";
			array[i] += Helper.ConvertHexByteToString(ReadMultipleBlockAnswer[sup]).toUpperCase();
			array[i] += " ";
			array[i] += Helper.ConvertHexByteToString(ReadMultipleBlockAnswer[sup + 1]).toUpperCase();
			array[i] += " ";
			array[i] += Helper.ConvertHexByteToString(ReadMultipleBlockAnswer[sup + 2]).toUpperCase();
			array[i] += " ";
			array[i] += Helper.ConvertHexByteToString(ReadMultipleBlockAnswer[sup + 3]).toUpperCase();
			sup += 4;
		}
		return array;
	}

}
