package ru.pda.cloudservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pda.cloudservice.components.JwtUtils;
import ru.pda.cloudservice.entitys.UserItemList;
import ru.pda.cloudservice.entitys.UserFile;
import ru.pda.cloudservice.repositorys.FileRepository;
import ru.pda.cloudservice.repositorys.UserRepository;

import java.util.List;

@Service
public class CloudService {

    Logger logger = LoggerFactory.getLogger(CloudService.class);
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;

    public boolean uploadFile(String token, String fileName, byte[] file) throws RuntimeException{
        try {
            Long userId = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token)).getId();
            fileRepository.save(new UserFile(userId, fileName, file));
        } catch (Exception e) {
            logger.error("Ошибка при загрузке файла.", e.getMessage());
            return false;
        }
        logger.info("Файл " + fileName + " успешно добавлен.");
        return true;
    }

    public UserFile downloadFile(String token, String fileName) {
        Long userId = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token)).getId();

        logger.info("Файл " + fileName + " успешно передан.");
        return fileRepository.findByUidAndFileName(userId, fileName);
    }

    public boolean updateFile(String token, String fileName, String name) throws RuntimeException{
        try {
            Long userId = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token)).getId();
            UserFile userFile = fileRepository.findByUidAndFileName(userId, fileName);
            UserFile newUserFile = new UserFile(userId, name, userFile.getFileContent());

            if (userFile == null || name == null) return false;

            fileRepository.delete(userFile);
            fileRepository.save(newUserFile);
        } catch (Exception e) {
            logger.error("Ошибка при переименовании файла.", e.getMessage());
            return false;
        }
        logger.info("Файл успешно переименован. " + fileName + " -> " + name);
        return true;
    }

    public boolean deleteFile(String token, String fileName) throws RuntimeException{
        try {
            Long userId = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token)).getId();
            UserFile userFile = fileRepository.findByUidAndFileName(userId, fileName);
            if (userFile == null) return false;
            fileRepository.delete(userFile);
        } catch (Exception e) {
            logger.error("Ошибка при удалении файла.", e.getMessage());
            return false;
        }
        logger.info("Файл " + fileName + " успешно удален.");
        return true;
    }

    public List<UserItemList> getFileList(int limit, String token) {
        Long userId = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token)).getId();
        List<UserItemList> userItems = fileRepository.findFileNameAndFileSizeByUid(userId, Pageable.ofSize(limit));
        logger.info("Список файлов успешно передан.");
        return userItems;
    }
}
