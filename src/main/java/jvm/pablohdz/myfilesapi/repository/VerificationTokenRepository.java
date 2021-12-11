package jvm.pablohdz.myfilesapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jvm.pablohdz.myfilesapi.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
