package com.vv.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.vv.model.Idea;
import com.vv.model.Profile;
@CrossOrigin
@RepositoryRestResource(excerptProjection = InlineProfile.class)
public interface IdeaRepository extends PagingAndSortingRepository<Idea, Long>{
	@Override
	List<Idea> findAll();
	@Query("SELECT documentName,videoName from Idea")
	List<Object> getIdeaFiles(Pageable pageable);
	@Query("SELECT i from Idea i where i.status = 'approve'")
	Page<Idea> findAllApprovedIdeas(Pageable pageable);
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Idea set rating = ?1, status = ?2 where profile_Id = ?3")
	int rateIdea(float rating,String status, Long id);
	Idea findOneById(Long id);
	@Query("SELECT i from Idea i where i.industry like %?1%" )
	List<Idea> getIdeasByVertical(String vertical);
	@Query("SELECT i from Idea i where i.areaOfFunc like %?1%" )
	List<Idea> getIdeasByProcess(String process);
	@Query("SELECT i from Idea i where i.industry like %?1% and i.areaOfFunc like %?2% ")
	List<Idea> getIdeasByVerPrcs(String industry, String process);
	@Query("SELECT i from Idea i where i.solnTitle like %?1% ")
	List<Idea> getIdeasByName(String soltitle);
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Idea set contbstatus = ?1 where id = ?2")
	int updateContributeStatus(String status, Long id);
	
	List<Idea> findByProfile(Profile pid);
	
	List<Idea> getIdeasBySolnTitleLike(String soltitle);
	List<Idea> getIdeasByareaOfFuncLike(String areaOfFunc);
	List<Idea> getIdeasByIndustryLike(String industry);
	List<Idea> getIdeasByIndustryLikeAndAreaOfFuncLike(String soltitle,String areaOfFunc);
	
	@RestResource(path = "findByEmail", rel = "findByEmail")
	public Page<Idea> getIdesByProfileCapEmailAndStatus(@Param("email") String email,@Param("status") String status, Pageable p);
	
}
