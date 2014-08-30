package anywheresoftware.b4a.objects;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Parcelable;
import android.util.Log;
import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA.Permissions;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.Version;
import anywheresoftware.b4a.objects.collections.List;

import com.nfc.common.NfcvData;
/**
 * Supports reading NDEF (NFC Data Exchange Format) tags.
 *See this <link>tutorial|http://www.basic4ppc.com/forum/basic4android-getting-started-tutorials/14931-reading-ndef-data-nfc-tags.html</link> for more information.
 */
@Version(1.23F)
@ShortName("NFC")
@Permissions(values={"android.permission.NFC"})
public class NFC
{
	
	private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1',
		(byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
		(byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
		(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };
	
	private static final String TAG = "B4A";	
    /**
     * Tests whether the Intent contains data read from an NDef tag.
     */	
  public boolean IsNdefIntent(Intent Intent)
  {
    if (Intent == null)
      return false;
    return Intent.hasExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
  }
  /*
  * Return Extra Tag
  */
  public String GetExtraTag(Intent Intent)
  {
    return "android.nfc.extra.TAG";
  }
  /*
   * Return NFC tag UID
   */
  public byte[] GeTagtUID(Intent Intent)
  {
    return Intent.getByteArrayExtra("android.nfc.extra.ID");
  }

  /**
   * Retrieves the NdefRecords stored in the Intent object.
   */
  
  public List GetNdefRecords(Intent Intent)
  {
    anywheresoftware.b4a.objects.collections.List l = new anywheresoftware.b4a.objects.collections.List();
    l.Initialize();
    Parcelable[] rawMsgs = Intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
    if (rawMsgs != null) {
      for (int i = 0; i < rawMsgs.length; i++) {
        NdefMessage nm = (NdefMessage)rawMsgs[i];
        for (NdefRecord r : nm.getRecords()) {
          l.Add(r);
        }
      }
    }
    return l;
  }

  @ShortName("NdefRecord")
  public static class NdefRecordWrapper extends AbsObjectWrapper<NdefRecord>
  {
    private static java.util.List<String> UriTypes = Arrays.asList(new String[] { 
      "", 
      "http://www.", 
      "https://www.", 
      "http://", 
      "https://", 
      "tel:", 
      "mailto:", 
      "ftp://anonymous:anonymous@", 
      "ftp://ftp.", 
      "ftps://", 
      "sftp://", 
      "smb://", 
      "nfs://", 
      "ftp://", 
      "dav://", 
      "news:", 
      "telnet://", 
      "imap:", 
      "rtsp://", 
      "urn:", 
      "pop:", 
      "sip:", 
      "sips:", 
      "tftp:", 
      "btspp://", 
      "btl2cap://", 
      "btgoep://", 
      "tcpobex://", 
      "irdaobex://", 
      "file://", 
      "urn:epc:id:", 
      "urn:epc:tag:", 
      "urn:epc:pat:", 
      "urn:epc:raw:", 
      "urn:epc:", 
      "urn:nfc:" });

    /**
     * Returns the whole payload
     */    
    public byte[] GetPayload()
    {
      return ((NdefRecord)getObject()).getPayload();
    }
    
    /**
     * Reads the payload and returns the stored text.
     */
    public String GetAsTextType()
      throws UnsupportedEncodingException
    {
      byte[] payload = getObject().getPayload();
      String textEncoding = (payload[0] & 0x80) == 0 ? "UTF-8" : "UTF-16";
      int languageCodeLength = payload[0] & 0x3F;
      @SuppressWarnings("unused")
      String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
      String text = 
        new String(payload, languageCodeLength + 1, 
        payload.length - languageCodeLength - 1, textEncoding);
      return text;
    }
    /**
     * Returns the variable length Type field
     */    
    public byte[] GetType()
    {
      byte[] Type = getObject().getType();
      return Type;
    }
    /*
    * Return the 3 bits Tnf
    */
    public short GetTnf()
    {
      return getObject().getTnf();
    }
    /*
    * Return the entire NDEF Record as a byte array
    */
    public byte[] GetNdefByteArray()
    {
      return getObject().toByteArray();
    }


    /*
    * Return True if Tag is of Uri type
    */ 
    public boolean IsUriType()
    {
      byte[] Type = getObject().getType();
      if (Type[0] == 85)
        return true;
      return false;
    }

    /*
    * Return True if Tag is of Text type
    */
    public boolean IsTextType()
    {
      byte[] Type = getObject().getType();
      if (Type[0] == 84)
        return true;
      return false;
    }
    
    /**
     * Reads the payload and returns the stored Uri.
     */ 
    public String GetAsUriType()
    {
      byte[] payload = getObject().getPayload();
      String prefix = (String)UriTypes.get(payload[0]);
      byte[] prefixBytes = prefix.getBytes(Charset.forName("UTF-8"));

      byte[] fullUri = new byte[prefixBytes.length + payload.length - 1];
      System.arraycopy(prefixBytes, 0, fullUri, 0, prefixBytes.length);
      System.arraycopy(payload, 1, fullUri, prefixBytes.length, payload.length - 1);
      Uri uri = Uri.parse(new String(fullUri, Charset.forName("UTF-8")));
      return uri.toString();
    }
  }

  @ShortName("MiFare")
  public static class MiFareWrapper extends AbsObjectWrapper<MifareClassic>
  {
	  private byte[] keyA;
	  private byte[] keyB;
	  private byte [][] block_data =  new byte[4][];
	  private int sectorIndex = 0;
	  
	  MifareClassic mfc;
	  private byte [][] knownKey = new byte[][]{
		MifareClassic.KEY_DEFAULT,
		MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY,
		MifareClassic.KEY_NFC_FORUM
	  };

	    /**
	     * Initialise the Mifare Classic object.
	     */  		
	  public void Initialize(Intent intent ){
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		mfc = MifareClassic.get(tagFromIntent);
		Log.i(TAG, "MF initializeded");	
	  	if(mfc == null){
			Log.i(TAG, "mfc null.Initialization failed");		  		
			return;
	  	}
/*		try {
			mfc.connect();
		} catch (IOException e) {
			Log.i(TAG, "mfc connect failed for IOExcetion");
			e.printStackTrace();
		}catch (IllegalStateException e){
			Log.i(TAG, "mfc connect failed");
			e.printStackTrace();
		}*/
		
	  }

  	/**
     * Read a sector. 
     * Parameter: 
     * 	sectorIndex, index of the sector to read
     *  key, the key for authentication. If it's null, 3 known default keys will be tested
     *  keyType, the type of key. 0 for keyA, 1 for keyB
     */	  
	  public boolean ReadSector(int sectorIndex ,byte[] Key, int keyType){
		  	this.sectorIndex = sectorIndex;
		  	if(connectMfc() == false) //Fail to connect
		  		return false;
				
			try {
				int firstBlockNo = mfc.sectorToBlock(sectorIndex);
				boolean auth = false;
				String cardData = null;
				auth = Authenticate(Key, keyType);
					
				if (auth) {
					for (int i = 0; i<4;i++){
						block_data[i] = mfc.readBlock(firstBlockNo + i);
						cardData = getHexString(block_data[i], block_data[i].length);
						if (cardData != null) {
						} else {
							Log.i(TAG, "No data in block " + i + " mfc closing before retrun...");
							mfc.close();
							return false; //No data
						}
					}
					Log.i(TAG, "Authenticated, a sector is read, mfc closing...");
					mfc.close();
				} else {
					Log.e(TAG, "Authenticate failed. mfc closing....");
					mfc.close();
					return false; //Auth failed
				}

			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				return false; //IO failed
			}			  
		  
		  return true;
	  }
	  
	/**
	 * Connect to a Mifare tag
	 * @return false if failed
	 */
	private boolean connectMfc() {
		if(mfc == null){
			Log.i(TAG, "mfc null. Please initialize the class first");
			return false;
		}
		if ( false == mfc.isConnected())
			try {
				mfc.connect();
			} catch (IllegalStateException e2){
				Log.i(TAG, "mfc connect failed,IllegalStateException");
				try {
					mfc.close();
				} catch (IOException e3) {
					
					e3.printStackTrace();
				}
				e2.printStackTrace();
				return false;					
			}catch (IOException e1) {
				Log.i(TAG, "mfc connect failed,IOException");
				e1.printStackTrace();
				return false;
			}
		return true;
	}
	  
	  	/**
	     * Write a sector. 
	     * Parameter: 
	     * 	sectorIndex, index of the sector to read
	     *  key, the key for authentication. If it's null, 3 known default keys will be tested
	     *  keyType, the type of key. 0 for keyA, 1 for keyB
	     */	  
		  public boolean WriteSector(int sectorIndex ,byte[] Key, int KeyType,byte [][] Data){
			  	this.sectorIndex = sectorIndex;
			  	if(connectMfc() == false) //Fail to connect
			  		return false;
					
				try {
					int firstBlockNo = mfc.sectorToBlock(sectorIndex);
					boolean auth = false;
					auth = Authenticate(Key, KeyType);
					if (auth) {
						for (int i = 0; i<4;i++){
							mfc.writeBlock(firstBlockNo + i , Data[i]);
						}
						Log.i(TAG, "Authenticated, a sector is written, mfc closing...");
						mfc.close();
					} else {
						
					
					}

				} catch (IOException e) {
					Log.e(TAG, e.getLocalizedMessage());
					return false; //IO failed
				}			  
					
					
		  
			  return false;
		  }
	  	/**
	     * Get 16 bytes data in a block. Should be called after a success reading
	     * Parameter: 
	     * 	BlockIndexInSector, index of the block inside it's block, should be 0 to 3 
	     */	  
	  
	  public byte [] GetBlockData(int blockIndexInSector){
		  blockIndexInSector = blockIndexInSector%4; //Make sure 0..3
		  
		  return block_data[blockIndexInSector];
	  }

	  public void SetBlockData(int blockIndexInSector, byte [] data){
		  blockIndexInSector = blockIndexInSector%4; //Make sure 0..3
		  block_data[blockIndexInSector] = data;
	  }
	  
	  
	  
	  public byte [] GetAllBlockData(){
		  //BlockIndexInSector = BlockIndexInSector%4; //Make sure 0..3
		  
		  //return block_data[BlockIndexInSector];
		  return null;
	  }	  
	  
	  private boolean Authenticate(byte [] key, int type) throws IOException{
		boolean success = false;
		byte [] userKey;
		if (key != null && key.length == 6 ){
			userKey = key;
			if (type == 0 )
				success = mfc.authenticateSectorWithKeyA(sectorIndex,userKey);
			else 
				success = mfc.authenticateSectorWithKeyB(sectorIndex,userKey);
			
			if (success)
				return true;
		}
		//Either failed or key is not used
		for(int i = 0;i<3;i++){
			if (type == 0 )
				success = mfc.authenticateSectorWithKeyA(sectorIndex,knownKey[i]);
			else 
				success = mfc.authenticateSectorWithKeyB(sectorIndex,knownKey[i]);
			//When there's match, report success	
			if (success)
				return true;			
		}
		return false; //Tried all the possibilities
	  }
  }//End MiFareWrapper

  /**
   * Tests whether the Intent is a Mifare classic tag.
   */	  
  public boolean IsMifareClassic(Intent intent)
  {
    if (intent == null)
      return false;
    
    boolean hasExtra = intent.hasExtra(NfcAdapter.EXTRA_TAG);
    boolean isMfc = false;
    if (hasExtra){
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		String strTechList[] = tagFromIntent.getTechList();
		for( String s :strTechList){
			
			if(s.contains("MifareClassic")){
				isMfc = true;
				break;
			}
		}	    
    }
    return isMfc;
  } 
  
  /**
   * Tests whether the Intent is a NfcV tag.
   */	  
  public boolean IsNfcv(Intent intent)
  {
	Log.i(TAG, "Checking Nfcv inside IsNfcv");  
    if (intent == null)
      return false;
    Log.i(TAG, "Intent is not null");
    boolean hasExtra = intent.hasExtra(NfcAdapter.EXTRA_TAG);
    boolean isNfcV = false;
    if (hasExtra){
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		String strTechList[] = tagFromIntent.getTechList();
		for( String s :strTechList){
			Log.i(TAG, "tech:" + s);
			if(s.contains("NfcV")){
				isNfcV = true;
				break;
			}
		}	    
    }
    Log.i(TAG, "return from IsNfcv()");
    return isNfcV;
  }
  
  /**
   * Tests whether the Intent is a NfcV tag.
   */
  public boolean IsV(Intent intent)
  {
		Log.i(TAG, "Checking Nfcv inside IsV");  
	    if (intent == null)
	        return false;
	    Log.i(TAG, "Intent is not null");	      
	      boolean hasExtra = intent.hasExtra(NfcAdapter.EXTRA_TAG);
	      boolean isNfcV = false;
	      if (hasExtra){
	  		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	  		String strTechList[] = tagFromIntent.getTechList();
	  		for( String s :strTechList){
	  			Log.i(TAG, "tech:" + s);
	  			if(s.contains("NfcV")){
	  				isNfcV = true;
	  				break;
	  			}
	  		}	    
	      }
	      return isNfcV;
  }
  /**
   * 
   * return an hex string of data
   */
  public static String getHexString(byte[] raw, int len) {
		byte[] hex = new byte[2 * len];
		int index = 0;
		int pos = 0;

		for (byte b : raw) {
			if (pos >= len)
				break;

			pos++;
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}
		return new String(hex);
  }
  
  @ShortName("Nfcv")
  public static class NfcvWrapper extends AbsObjectWrapper<NfcvData>
  {
	  NfcvData nd;
	  public void Initialize(Intent intent ){
		  nd = new NfcvData();
		  Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		  nd.setCurrentTag(tagFromIntent);
		  Log.i(TAG, "Nfcv data initialized");		  		
	  
	  }

 	/**
	 * Send command to get information about the tag
     */		  
	  public boolean getSystemInfo(){ 
	  	return nd.getSystemInfo();
	  }
 	/**
     * Read a tag. 
     * Parameter: 
     * 	addressStart, byte array with address in big endian
     *  numberOfBlockToRead, byte array with number of blocks to read, in big endian
     */		  
	  public void readTag(byte[] addressStart, byte[] numberOfBlockToRead){
		  nd.readTag(addressStart, numberOfBlockToRead);
	  }
	  
	  
 	/**
     * Get the result of reading. 
     */		  
	  public byte[] getReadData(){
		  return nd.getReadData();
	  }
	  
	  /**
	   * Get the UID of the tag 
	   */
	  
	  public String getUid() {
		return nd.getUid();
	  }

	  /**
	   * Get the uid in bytes 
	   */
	  public byte[] getUidRaw() {
		return nd.getUidRaw();
	  }

	  
	  /**
	   * Get the tech of the tag, i.e. ISO 15693
	   */
		public String getTechno() {
			return nd.getTechno();
		}

		/**
		 *	Get the manufacturer of the tag chip 
		 */
		public String getManufacturer() {
			return nd.getManufacturer();
		}

		/**
		 * Get the product name
		 */
		public String getProductName() {
			return nd.getProductName();
		}

		/**
		 * Get the dsfid of the tag 
		 */
		public String getDsfid() {
			return nd.getDsfid();
		}

		/**
		 * Get AFI
		 */
		public String getAfi() {
			return nd.getAfi();
		}
		/**
		 * Get number of blocks
		 */

		public String getMemorySize() {
			return nd.getMemorySize();
		}

		/**
		 * Get the block size. 
		 */
		
		public String getBlockSize() {
			return nd.getBlockSize();
		}

		/**
		 * Get the IC reference
		 */
		public String getIcReference() {
			return nd.getIcReference();
		}

		public boolean isBasedOnTwoBytesAddress() {
			return nd.isBasedOnTwoBytesAddress();
		}


		public boolean isMultipleReadSupported() {
			return nd.isMultipleReadSupported();
		}	
		


		public boolean isMemoryExceed2048bytesSize() {
			return nd.isMemoryExceed2048bytesSize();
		}		  
   }
}
