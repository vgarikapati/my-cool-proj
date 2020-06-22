package com.optum.micro.domain.member.das;

import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RedisHash("validationrules")
public class DataAccessConfig  extends DataAccessConfigDTO{

	
}
