/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.act.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.jeeplus.modules.act.entity.Act;
import com.jeeplus.modules.act.utils.ProcessDefCache;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.BaseService;

/**
 * ??????????????????Controller
 * @author jeeplus
 * @version 2016-11-03
 */
@Service
@Transactional(readOnly = true)
public class ActProcessService extends BaseService {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;	
	@Autowired
	private HistoryService historyService;

	/**
	 * ??????????????????
	 */
	public Page<Map> processList(Page<Map> page, String category) {

	    ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
	    		.latestVersion().orderByProcessDefinitionKey().asc();
	    
	    if (StringUtils.isNotEmpty(category)){
	    	processDefinitionQuery.processDefinitionCategory(category);
		}
	    
	    page.setCount(processDefinitionQuery.count());
	    
	    List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(page.getFirstResult(), page.getMaxResults());
	    for (ProcessDefinition processDefinition : processDefinitionList) {
	      String deploymentId = processDefinition.getDeploymentId();
	      Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
	      Map pMap = new HashMap<>();
	      pMap.put("id", processDefinition.getId());
	      pMap.put("category", processDefinition.getCategory());
	      pMap.put("key", processDefinition.getKey());
	      pMap.put("name", processDefinition.getName());
	      pMap.put("version","V:"+processDefinition.getVersion());
	      pMap.put("resourceName", processDefinition.getResourceName());
	      pMap.put("diagramResourceName", processDefinition.getDiagramResourceName());
	      pMap.put("deploymentId", processDefinition.getDeploymentId());
	      pMap.put("suspended", processDefinition.isSuspended());
			pMap.put("deploymentTime",deployment.getDeploymentTime());
	      page.getList().add(pMap);
	    }

		return page;
	}

	/**
	 * ??????????????????
	 */
	public Page<HashMap<String,String>> runningList(Page<ProcessInstance> page, String procInsId, String procDefKey) {
		List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		Page<HashMap<String,String>> resultPage = new Page<>();
	    ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();

	    if (StringUtils.isNotBlank(procInsId)){
		    processInstanceQuery.processInstanceId(procInsId);
	    }
	    
	    if (StringUtils.isNotBlank(procDefKey)){
		    processInstanceQuery.processDefinitionKey(procDefKey);
	    }
	    
	    page.setCount(processInstanceQuery.count());
		resultPage.setCount(processInstanceQuery.count());
		List<ProcessInstance> runningList = processInstanceQuery.listPage(page.getFirstResult(), page.getMaxResults());

		for (ProcessInstance p : runningList) {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("id", p.getId());
			map.put("processDefinitionName", p.getProcessDefinitionName());
			map.put("processInstanceId", p.getProcessInstanceId());
			map.put("processDefinitionId", p.getProcessDefinitionId());
			map.put("activityId", p.getActivityId());
			map.put("suspended", String.valueOf(p.isSuspended()));
			result.add(map);
		}
		resultPage.setList(result);
		return resultPage;
	}
	

	/**
	 * ??????????????????
	 */
	public Page<HistoricProcessInstance> historyList(Page<HistoricProcessInstance> page, String procInsId, String procDefKey) {

		HistoricProcessInstanceQuery query=historyService.createHistoricProcessInstanceQuery().finished()
		.orderByProcessInstanceEndTime().desc();

	    if (StringUtils.isNotBlank(procInsId)){
	    	query.processInstanceId(procInsId);
	    }
	    
	    if (StringUtils.isNotBlank(procDefKey)){
	    	query.processDefinitionKey(procDefKey);
	    }
	    
	    page.setCount(query.count());
	    page.setList(query.listPage(page.getFirstResult(), page.getMaxResults()));
		return page;
	}
	
	/**
	 * ???????????????????????????ID
	 */
	public InputStream resourceRead(String procDefId, String proInsId, String resType) throws Exception {
		
		if (StringUtils.isBlank(procDefId)){
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proInsId).singleResult();
			procDefId = processInstance.getProcessDefinitionId();
		}
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
		
		String resourceName = "";
		if (resType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}
		
		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
		return resourceAsStream;
	}
	
	/**
	 * ???????????? - ??????
	 * @param file
	 * @return
	 */
	@Transactional(readOnly = false)
	public String deploy(String exportDir, String category, MultipartFile file) {

		String message = "";
		
		String fileName = file.getOriginalFilename();
		
		try {
			InputStream fileInputStream = file.getInputStream();
			Deployment deployment = null;
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
			} else if (extension.equals("png")) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			} else if (fileName.indexOf("bpmn20.xml") != -1) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			} else if (extension.equals("bpmn")) { // bpmn?????????????????????????????????bpmn20.xml
				String baseName = FilenameUtils.getBaseName(fileName); 
				deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
			} else {
				message = "???????????????????????????" + extension;
			}
			
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

			// ??????????????????
			for (ProcessDefinition processDefinition : list) {
//					ActUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
				message += "?????????????????????ID=" + processDefinition.getId() + "<br/>";
			}
			
			if (list.size() == 0){
				message = "??????????????????????????????";
			}
			
		} catch (Exception e) {
			throw new ActivitiException("???????????????", e);
		}
		return message;
	}
	
	/**
	 * ??????????????????
	 */
	@Transactional(readOnly = false)
	public void updateCategory(String procDefId, String category) {
		repositoryService.setProcessDefinitionCategory(procDefId, category);
	}

	/**
	 * ???????????????????????????
	 */
	@Transactional(readOnly = false)
	public String updateState(String state, String procDefId) {
		if (state.equals("active")) {
			repositoryService.activateProcessDefinitionById(procDefId, true, null);
			return "?????????ID???[" + procDefId + "]??????????????????";
		} else if (state.equals("suspend")) {
			repositoryService.suspendProcessDefinitionById(procDefId, true, null);
			return "?????????ID???[" + procDefId + "]??????????????????";
		}
		return "?????????";
	}
	
	/**
	 * ?????????????????????????????????
	 * @param procDefId
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException
	 */
	@Transactional(readOnly = false)
	public org.activiti.engine.repository.Model convertToModel(String procDefId) throws UnsupportedEncodingException, XMLStreamException {
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
		InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
		processDefinition.getResourceName());
		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
		XMLStreamReader xtr = xif.createXMLStreamReader(in);
		BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
	
		BpmnJsonConverter converter = new BpmnJsonConverter();
		ObjectNode modelNode = converter.convertToJson(bpmnModel);
		org.activiti.engine.repository.Model modelData = repositoryService.newModel();
		modelData.setKey(processDefinition.getKey());
		modelData.setName(processDefinition.getResourceName());
		modelData.setCategory(processDefinition.getCategory());//.getDeploymentId());
		modelData.setDeploymentId(processDefinition.getDeploymentId());
		modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count()+1)));
	
		ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
		modelData.setMetaInfo(modelObjectNode.toString());
	
		repositoryService.saveModel(modelData);
	
		repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
	
		return modelData;
	}
	
	/**
	 * ???????????????????????????
	 */
	public List<String> exportDiagrams(String exportDir) throws IOException {
		List<String> files = new ArrayList<String>();
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		
		for (ProcessDefinition processDefinition : list) {
			String diagramResourceName = processDefinition.getDiagramResourceName();
			String key = processDefinition.getKey();
			int version = processDefinition.getVersion();
			String diagramPath = "";

			InputStream resourceAsStream = repositoryService.getResourceAsStream(
					processDefinition.getDeploymentId(), diagramResourceName);
			byte[] b = new byte[resourceAsStream.available()];

			@SuppressWarnings("unused")
			int len = -1;
			resourceAsStream.read(b, 0, b.length);

			// create file if not exist
			String diagramDir = exportDir + "/" + key + "/" + version;
			File diagramDirFile = new File(diagramDir);
			if (!diagramDirFile.exists()) {
				diagramDirFile.mkdirs();
			}
			diagramPath = diagramDir + "/" + diagramResourceName;
			File file = new File(diagramPath);

			// ??????????????????
			if (file.exists()) {
				// ?????????????????????????????????????????????????????????(????????????)
				logger.debug("diagram exist, ignore... : {}", diagramPath);
				
				files.add(diagramPath);
			} else {
				file.createNewFile();
				logger.debug("export diagram to : {}", diagramPath);

				// wirte bytes to file
				FileUtils.writeByteArrayToFile(file, b, true);
				
				files.add(diagramPath);
			}
			
		}
		
		return files;
	}

	/**
	 * ????????????????????????????????????????????????
	 * @param deploymentId ????????????ID
	 */
	@Transactional(readOnly = false)
	public void deleteDeployment(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
	}
	
	/**
	 * ???????????????????????????
	 * @param procInsId ????????????ID
	 * @param deleteReason ????????????????????????
	 */
	@Transactional(readOnly = false)
	public void deleteProcIns(String procInsId, String deleteReason) {
		runtimeService.deleteProcessInstance(procInsId, deleteReason);
	}
	
}
