package serviceTests;
import dataAccess.DataAccessException;
import dataAccess.IAuthDAO;
import dataAccess.Memory.MemoryAuthDAO;
import dataAccess.Memory.MemoryDataAccess;
import dataAccess.Memory.MemoryUserDAO;
import model.AuthModel;
import model.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.JsonRequestObjects.RegisterRequest;
import service.ChessService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChessServiceTest {

    static final ChessService service = new ChessService(new MemoryDataAccess());

    @BeforeEach
    void clearBefore() {
        service.deleteAll();
    }
    @AfterEach
    void clearAfter() {
        service.deleteAll();
    }

    @Test
    void registerUserSuccess() throws DataAccessException {
        String inputUsername = "Hey I'm Bob";
        RegisterRequest inputRegisterObject = new RegisterRequest(inputUsername, "abc123", "a@b.com");

        String outputRegisterObject = service.registerUser(inputRegisterObject);
        AuthModel criteriaRegisterObject = new MemoryAuthDAO().getRowByAuthtoken(outputRegisterObject);

        String criteriaUsername = criteriaRegisterObject.username();
        assertEquals(inputUsername, criteriaUsername);

        List<UserModel> userModelList = new MemoryUserDAO().findAll(model -> model.username().equals(inputUsername));
        assertEquals(1, userModelList.size());
    }
    @Test
    void registerUserFailure() throws DataAccessException {
        String inputUsername = "Hey I'm Bob";
        RegisterRequest inputRegisterObject = new RegisterRequest(inputUsername, "abc123", "a@b.com");
        String firstMethodCall = service.registerUser(inputRegisterObject);

        assertThrows(DataAccessException.class, () -> service.registerUser(inputRegisterObject));
    }


//    @Test
//    void listPets() throws ResponseException {
//        List<Pet> expected = new ArrayList<>();
//        expected.add(service.addPet(new Pet(0, "joe", PetType.FISH)));
//        expected.add(service.addPet(new Pet(0, "sally", PetType.CAT)));
//        expected.add(service.addPet(new Pet(0, "fido", PetType.DOG)));
//
//        var actual = service.listPets();
//        assertIterableEquals(expected, actual);
//    }
//
//    @Test
//    void deletePet() throws ResponseException {
//        List<Pet> expected = new ArrayList<>();
//        var pet = service.addPet(new Pet(0, "joe", PetType.FISH));
//        expected.add(service.addPet(new Pet(0, "sally", PetType.CAT)));
//        expected.add(service.addPet(new Pet(0, "fido", PetType.DOG)));
//
//        service.deletePet(pet.id());
//        var actual = service.listPets();
//        assertIterableEquals(expected, actual);
//    }
//
//    @Test
//    void deleteAllPets() throws ResponseException {
//        service.addPet(new Pet(0, "joe", PetType.FISH));
//        service.addPet(new Pet(0, "sally", PetType.CAT));
//        service.addPet(new Pet(0, "fido", PetType.DOG));
//
//        service.deleteAllPets();
//        assertEquals(0, service.listPets().size());
//    }




}
