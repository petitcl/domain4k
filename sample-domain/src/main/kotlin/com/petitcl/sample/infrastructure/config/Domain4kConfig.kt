package com.petitcl.sample.infrastructure.config

import com.petitcl.domain4k.spring.Domain4kSpringAdapter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(Domain4kSpringAdapter::class)
@Configuration
class Domain4kConfig
