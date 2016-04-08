package jp.co.bbs.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.bbs.dto.BranchDto;
import jp.co.bbs.entity.Branch;
import jp.co.bbs.mapper.BranchMapper;

@Service
public class BranchService {

	@Autowired
    private BranchMapper branchMapper;
	
	public List<BranchDto> getBranch() {
		List<Branch> branchList = branchMapper.getBranch();
		List<BranchDto> resultList = convertToDto(branchList);
		return resultList;
	}
	private List<BranchDto> convertToDto(List<Branch> branchList) {
	    List<BranchDto> resultList = new LinkedList<BranchDto>();
	    for (Branch entity : branchList) {
	    	BranchDto dto = new BranchDto();
	        BeanUtils.copyProperties(entity, dto);
	        resultList.add(dto);
	    }
	    return resultList;
	}
}
