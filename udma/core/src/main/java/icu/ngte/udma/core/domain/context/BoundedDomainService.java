package icu.ngte.udma.core.domain.context;

public interface BoundedDomainService<T extends BoundedContext> extends DomainService {

  T context();
}
