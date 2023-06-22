package ru.pda.cloudservice.repositorys;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import ru.pda.cloudservice.entitys.AuthUser;
import ru.pda.cloudservice.entitys.UserFile;
import ru.pda.cloudservice.entitys.UserItemList;
import ru.pda.cloudservice.services.CloudService;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RepositoryTest {
    private final UserFile uf = new UserFile(1L, "test.txt", "Hello World".getBytes());
    private final AuthUser au = new AuthUser(1L, "user@home.ru", "123", "USER");

    private final List<UserItemList> uiList = new ArrayList<>();


    @InjectMocks
    @Autowired
    private CloudService cs;
    @Mock
    @Autowired
    private UserRepository ur;
    @Mock
    @Autowired
    private FileRepository fr;

    @BeforeEach
    void setUp() {
        Mockito.when(ur.save(au));
//        Mockito.when(ur.findByUsername("user@home.ru").getId()).thenReturn(1L);
        Mockito.when(fr.save(uf)).thenReturn(uf);
    }

    @Test
    public void testUser () {
        Mockito.when(ur.findByUsername("user@home.ru").getId()).thenReturn(1L);
    }

    @Test
    public void testFile() {
        Mockito.when(fr.findByUidAndFileName(1L, "test.txt")).thenReturn(uf);
    }

    @Test
    public void test() {
        Mockito.when(fr.findFileNameAndFileSizeByUid(1L, Pageable.ofSize(3))).thenReturn(uiList);
    }
}
