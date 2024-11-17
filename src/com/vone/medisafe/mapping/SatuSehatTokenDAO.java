package com.vone.medisafe.mapping;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class SatuSehatTokenDAO extends HibernateDaoSupport {

    public SatuSehatToken getToken(){
        Session session = null;
        SatuSehatToken result = null;
        String query = "select {tok.*} from satu_sehat_token tok";
        try {
            session = super.getSessionFactory().openSession();
            result = (SatuSehatToken) session.createSQLQuery(query)
                    .addEntity("tok", SatuSehatToken.class)
                    .uniqueResult();

        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(session != null)session.close();
        }
        return result;
    }

    public void save(SatuSehatToken token){
        getHibernateTemplate().saveOrUpdate(token);
    }
}
