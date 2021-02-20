package com.udma.core.domain.context;

public interface BoundedDomainService<T extends BoundedContext> extends DomainService {

  T context();
}
