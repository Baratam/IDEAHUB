package com.vv.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.vv.model.Contributions;
import com.vv.model.Idea;
import com.vv.repositories.ContributeRepository;
import com.vv.repositories.IdeaRepository;
import com.vv.service.StorageService;
import com.vv.utils.FileNameBuilder;

@Controller
public class ContributionController {
	private final Logger log = LoggerFactory.getLogger(IdeaContoller.class);
	private ContributeRepository contributionRepository;
	private IdeaRepository ideaRepository;
	private StorageService storageService;
	
	public ContributionController(ContributeRepository contributionRepository, IdeaRepository ideaRepository,StorageService storageService) {
		super();
		this.contributionRepository = contributionRepository;
		this.ideaRepository = ideaRepository;
		this.storageService = storageService;
	}
	
	@PostMapping(value = "/contribute", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> contributeIdea(@RequestBody HashMap<String,List<String>> data) {
		Idea idea = ideaRepository.findOneById(Long.parseLong(data.get("team").get(0)));
		List<Contributions> contribList = new ArrayList<Contributions>();
		for(int i=1;i<=data.size()-1;i++){
			contribList.add(new Contributions(idea, data.get("team").get(1),Byte.valueOf(data.get("team").get(2)),data.get("member"+i).get(0),data.get("member"+i).get(1),data.get("member"+i).get(2),data.get("member"+i).get(3),data.get("member"+i).get(4),data.get("member"+i).get(5).charAt(0)));
			System.out.println("-->1.   "+data.get("team").get(1));
			System.out.println("-->2.   "+Byte.valueOf(data.get("team").get(2)));
			System.out.println("-->3.   "+data.get("member"+i).get(0));
			System.out.println("-->4.   "+data.get("member"+i).get(1));
			System.out.println("-->5.   "+data.get("member"+i).get(2));
			System.out.println("-->6.   "+data.get("member"+i).get(3));
			System.out.println("-->7.   "+data.get("member"+i).get(4));
			System.out.println("-->8.   "+data.get("member"+i).get(5).charAt(0));
		}
		Stream<Contributions> contributions = contribList.stream();
		try{
		contributions.forEach(contributionRepository::save);
		ideaRepository.updateContributeStatus("Submited",idea.getId());
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
			return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	@PostMapping(value = "/submitContrib")
	public ResponseEntity<String> submitContribution(@RequestParam("publishDoc") MultipartFile doc,@RequestParam("publishVideo") MultipartFile video,@RequestParam("ideaId") String id) {
		String docFileName = null;
		String videoFileName = null;
		try{
			Idea idea = ideaRepository.findOneById(Long.parseLong(id));
			docFileName = String.format("%04d", new Random().nextInt(10000))+"_d";
			if(video != null && video.isEmpty() && doc != null && doc.isEmpty()){
				throw new Exception("Invalid details");
			}else{
				docFileName = String.format("%04d", new Random().nextInt(10000))+"_d";
				videoFileName = String.format("%04d", new Random().nextInt(10000))+"_v";
				storageService.storeIdea(doc,docFileName,video,videoFileName);
				idea.setStatus("published");
				idea.setPublishDoc(docFileName);
				idea.setPublishVideo(videoFileName);
				ideaRepository.save(idea);
				contributionRepository.setContribActivity("I", Long.parseLong(id));
			}
			System.out.println(doc.getOriginalFilename()+"  "+video.getOriginalFilename());
		//ideaRepository.updateContributeStatus("Submited",idea.getId());
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
			return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	
}
