package com.lyubenblagoev.postfixrest.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lyubenblagoev.postfixrest.FileUtils;
import com.lyubenblagoev.postfixrest.configuration.MailServerConfiguration;
import com.lyubenblagoev.postfixrest.entity.Domain;
import com.lyubenblagoev.postfixrest.repository.DomainRepository;
import com.lyubenblagoev.postfixrest.service.model.DomainResource;

@Service
@Transactional(readOnly = true)
public class DomainServiceImpl implements DomainService {
	
	private final DomainRepository domainRepository;
	private final MailServerConfiguration mailServerConfiguration;
	
	public DomainServiceImpl(DomainRepository domainRepository, MailServerConfiguration mailServerConfiguration) {
		this.domainRepository = domainRepository;
		this.mailServerConfiguration = mailServerConfiguration;
	}
	
	@Override
	public List<DomainResource> getAllDomains() {
		Iterable<Domain> entities = domainRepository.findAll();
		List<DomainResource> domains = new ArrayList<>();
		entities.forEach(e -> domains.add(new DomainResource(e.getId(), e.getName(), e.isEnabled(), e.getCreated(), e.getUpdated())));
		return domains;
	}

	@Override
	public DomainResource getDomainByName(String name) {
		Domain entity = domainRepository.findByName(name);
		if (entity == null) {
			throw new DomainNotFoundException("no domain with name " + name);
		}
		return new DomainResource(entity.getId(), entity.getName(), entity.isEnabled(), entity.getCreated(), entity.getUpdated());
	}

	@Override
	@Transactional
	public DomainResource save(DomainResource domain) {
		if (domain.getId() == null && domainRepository.findByName(domain.getName()) != null) {
			throw new DomainExistsException("domain with that name already exist: " + domain.getName());
		}
		
		Domain entity = domain.getId() == null ? new Domain() : domainRepository.findById(domain.getId()).get();

		if (domain.getId() != null && !entity.getName().equals(domain.getName())) {
			FileUtils.renameFolder(new File(mailServerConfiguration.getVhostsPath()), entity.getName(), domain.getName());
		}

		entity.setName(domain.getName());
		if (domain.getEnabled() != null) {
			entity.setEnabled(domain.getEnabled()); 
		}
		entity.setUpdated(new Date());

		entity = domainRepository.save(entity);

		return new DomainResource(entity.getId(), entity.getName(), entity.isEnabled(), entity.getCreated(), entity.getUpdated());
	}

	@Override
	@Transactional
	public void delete(String name) {
		Domain domain = domainRepository.findByName(name);
		if (domain == null) {
			throw new DomainNotFoundException("domain not found: " + name);
		}
		FileUtils.deleteFolder(new File(mailServerConfiguration.getVhostsPath()), name);
		domainRepository.delete(domain);
	}

}
