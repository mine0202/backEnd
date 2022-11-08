package com.example.simpledms.service;

import com.example.simpledms.model.Customer;
import com.example.simpledms.model.Faq;
import com.example.simpledms.repository.CustomerRepository;
import com.example.simpledms.repository.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 부서업무 서비스 클래스
@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    //    저장함수
    public Customer save(Customer customer) {
        Customer customer1 = customerRepository.save(customer);
        return customer1;
    }

//  ID 로 조회 함수
    public Optional findById(int cid){
        Optional<Customer> optionalCustomer = customerRepository.findById(cid);
        return optionalCustomer;
    }

    public boolean removeById(int cid) {
//        있으면 삭제 실행
        if(customerRepository.existsById(cid)) {
            customerRepository.deleteById(cid);
            return true;
        }
//        없으면 false 리턴
        return false;
    }


    //    조회 함수
    public Page<Customer> findAllByEmailContaining(String email, Pageable pageable) {
        Page<Customer> page = customerRepository.findAllByEmailContaining(email,pageable);
        return page;
    }

}
