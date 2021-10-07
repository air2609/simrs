package com.vone.medisafe.common.util;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.ConfigurationManager;


import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Created by JiM
 * User: HP compaq nx5000
 * Date: Jul 24, 2006
 * Time: 12:16:15 AM
 */
public class ZKPathReader {

    private static ZKPathReader instance = null;

    private Map reportMap = new Hashtable();
    private static final String stFile = ConfigurationManager.getKey("zul.action.path");
//    private static final String stFile = "/tomcat5/webapps/medisafe/config/zul.path.config.xml";
    private File fileConfig = new File(stFile);
    private static long fileID;

    private ZKPathReader() {}

    public synchronized static ZKPathReader getInstance() throws VONEAppException
    {
      try {
		if (instance == null) {
			instance = new ZKPathReader();
			instance.load();
		}
		//check latest file version
		File obFile = new File(stFile);
		if (fileID != obFile.lastModified())
			instance.load();
		return instance;
	} catch (Exception e) {
		// TODO: handle exception Auto-generated
		e.printStackTrace();
		throw new VONEAppException(e.getMessage()); 
	}
    }

    /**
     * this method will return URL of specified ZK Name
     * @param stId
     * @param stDivision
     * @return
     */
    public String getUrl(String stZKName){
        return (String)reportMap.get(stZKName);
    }

    public void load() throws JDOMException, IOException
    {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(fileConfig);
        fileID = fileConfig.lastModified();
        Element el = doc.getRootElement();

        List zk = el.getChildren("zk");
        Iterator i = zk.iterator();
        while (i.hasNext()) {
            Element zkElement = (Element)i.next();
            String zkName = zkElement.getAttributeValue("name");
            
            List path = zkElement.getChildren();
            Iterator i2 = path.iterator();
                while (i2.hasNext()) {
                    Element urlElement = (Element)i2.next();
                    String url = urlElement.getAttributeValue("url");

                    reportMap.put(zkName, url);
                }
        }
    }
    
}