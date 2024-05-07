package com.learningSpringBoot.FirstSpring.company.impl;

import com.learningSpringBoot.FirstSpring.company.Company;
import com.learningSpringBoot.FirstSpring.company.CompanyRepository;
import com.learningSpringBoot.FirstSpring.company.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepository;
    private Long nextId = 1L;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public void createCompany(Company company) {
        company.setId(nextId++);
        companyRepository.save(company);
    }

    @Override
    public boolean updateCompany(Company updatedCompany, Long id) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            company.setName(updatedCompany.getName());
            company.setDescription(updatedCompany.getDescription());
            companyRepository.save(company);

            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCompanyById(Long id) {
       if(companyRepository.existsById(id)){
            companyRepository.deleteById(id);
            return true;
        }
       return false;

    }

    @Override
    public Company getCompanyById(Long id) {
       return companyRepository.findById(id).orElse(null);
    }


}
