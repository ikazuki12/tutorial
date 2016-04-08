package jp.co.bbs.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.bbs.dto.PositionDto;
import jp.co.bbs.entity.Position;
import jp.co.bbs.mapper.PositionMapper;

@Service
public class PositionService {

	@Autowired
	private PositionMapper positionMapper;
	
	public List<PositionDto> getPosition() {
		List<Position> positionList = positionMapper.getPosition();
		List<PositionDto> resultList = convertToDto(positionList);
		return resultList;
	}
	
	private List<PositionDto> convertToDto(List<Position> positionList) {
		List<PositionDto> resultList = new LinkedList<PositionDto>();
		for (Position entity : positionList) {
			PositionDto dto = new PositionDto();
			BeanUtils.copyProperties(entity, dto);
			resultList.add(dto);
		}
		return resultList;
	}
}
