package ru.pda.cloudservice.repositorys;


import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pda.cloudservice.entitys.UserFile;

import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<UserFile, Id> {
    UserFile findByUidAndFileName(Long uid, String filename);
    List<UserFile> findFileNameByUid(Long uid, Pageable pageable);
}