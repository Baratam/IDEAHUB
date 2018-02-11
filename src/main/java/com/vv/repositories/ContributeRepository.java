package com.vv.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.vv.model.Contributions;

public interface ContributeRepository extends PagingAndSortingRepository<Contributions, Long> {
	@Override
	List<Contributions> findAll();
	List<Contributions> findByideaId(Long idea_id);
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Contributions set active = ?1 where idea_id = ?2")
	int setContribActivity(String status, Long id);
}
