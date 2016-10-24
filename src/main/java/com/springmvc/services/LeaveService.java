package com.springmvc.services;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springmvc.models.Leave;

@Service
public class LeaveService implements ILeaveService {

	@PersistenceContext EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<Leave> findApplyList(Long applyPersonId, Pageable pageable) {
		String sql = "select l.*"
				+ " from leaves l"
				+ " inner join act_hi_procinst p on p.PROC_INST_ID_=l.process_instance_id";
        List<Leave> list = em.createNativeQuery(sql, Leave.class).getResultList();
        
        String countSql = "select count(id) from leaves";
        BigInteger count = (BigInteger) em.createNativeQuery(countSql).getSingleResult();
		
        Page<Leave> page = new PageImpl<Leave>(list, pageable, count.longValue());
		return page;
	}
	
	
	
}
