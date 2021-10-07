package com.vone.medisafe.mapping;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vone.medisafe.common.exception.VONEAppException;
import com.vone.medisafe.common.services.MessagesService;

public class TbBedTrxDAO extends HibernateDaoSupport{
	
	Logger logger = Logger.getLogger(TbBedTrxDAO.class);
	
	public TbBedTrx getBTrxByNota(TbExamination nota) throws VONEAppException{
		TbBedTrx result = null;
		Session session = null;
		
		String sql = "select {btrx.*} from tb_bed_trx btrx where n_note_id = :nota";
		
		try {
			session = super.getSessionFactory().openSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("btrx", TbBedTrx.class);
			query.setEntity("nota", nota);			
			result = (TbBedTrx)query.uniqueResult();
			
		} catch (HibernateException e) {
        	logger.error("getBTrxByNota failed", e);
            throw new VONEAppException(MessagesService.getKey("error.nodata"), e.getMessage());
		}finally{
			if(session != null)session.close();
		}
		
		return result;
	}    
    
    public void save (MsBank pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().save(pojo);
    	}catch (Exception e){
    		logger.error("save error", e);
    		throw new VONEAppException(MessagesService.getKey("error.saveadd"));
    	}
    }
    
    public void update (MsBank pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().update(pojo);
    	}catch (Exception e){
    		logger.error("update error", e);
    		throw new VONEAppException(MessagesService.getKey("error.savemodify"));
    	}
    }
    
    public void delete (MsBank pojo) throws VONEAppException {
    	
    	try {
    		getHibernateTemplate().delete(pojo);
    	}catch (Exception e){
    		logger.error("delete error", e);
    		throw new VONEAppException(MessagesService.getKey("error.delete"));
    	}
    }
}
