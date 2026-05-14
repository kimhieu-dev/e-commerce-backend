package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.response.TrackingLogRes;
import com.nkh.ecommercebackend.entity.TrackingLog;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackingLogMapper {
    List<TrackingLogRes> toTrackingLogResList(List<TrackingLog> trackingLogs);
}
