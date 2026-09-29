package com.jobsphere.company.service;

import com.jobsphere.company.dto.CompanyResponse;
import com.jobsphere.company.entity.Company;
import com.jobsphere.company.dto.CompanyRequest;
import com.jobsphere.company.repository.CompanyRepository;
import com.jobsphere.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponse createCompany(CompanyRequest request) {

        Company company = new Company();

        company.setName(request.getName());
        company.setIndustry(request.getIndustry());
        company.setLocation(request.getLocation());
        company.setWebsite(request.getWebsite());
        company.setDescription(request.getDescription());

        Company savedCompany = companyRepository.save(company);

        return mapToResponse(savedCompany);
    }

    public List<CompanyResponse> getAllCompanies() {

        return companyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CompanyResponse getCompanyById(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: " + id
                        ));

        return mapToResponse(company);
    }

    public CompanyResponse updateCompany(Long id, CompanyRequest request) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: " + id
                        ));

        company.setName(request.getName());
        company.setIndustry(request.getIndustry());
        company.setLocation(request.getLocation());
        company.setWebsite(request.getWebsite());
        company.setDescription(request.getDescription());

        Company updatedCompany = companyRepository.save(company);

        return mapToResponse(updatedCompany);
    }

    public void deleteCompany(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Company not found with id: " + id));

        companyRepository.delete(company);
    }

    private CompanyResponse mapToResponse(Company company) {

        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getIndustry(),
                company.getLocation(),
                company.getWebsite(),
                company.getDescription(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }
}