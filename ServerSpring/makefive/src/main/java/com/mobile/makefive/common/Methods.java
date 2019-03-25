package com.mobile.makefive.common;

import com.mobile.makefive.entity.TblAccount;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Methods {

    public Methods() {
    }

    public long getTimeNow() {
        return new Date().getTime();
    }

    public String getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<? extends GrantedAuthority> roles = (List<? extends GrantedAuthority>) authentication.getAuthorities();
        return roles.get(0).getAuthority();
    }

    public TblAccount getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(principal);
        return (TblAccount) principal;
    }

    public String hashPass(String input) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(input, salt);
    }

    public int getAge(long time) {
        Calendar firstCal = GregorianCalendar.getInstance();
        Calendar secondCal = GregorianCalendar.getInstance();
        firstCal.setTimeInMillis(time);
        secondCal.setTime(new Date());
        secondCal.add(Calendar.DAY_OF_YEAR, 1 - firstCal.get(Calendar.DAY_OF_YEAR));
        return secondCal.get(Calendar.YEAR) - firstCal.get(Calendar.YEAR);
    }


    public String handleImage(MultipartFile image) {
        if (image != null) {
            String fileName = image.getOriginalFilename();
//            Files.createDirectories(rootLocation);
            try {
                Files.copy(image.getInputStream(), Fix.IMG_DIR_PATH.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String bytesToBase64(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
        return sb.toString();
    }

    public byte[] multipartToBytes(MultipartFile input) {
        if (input != null) {
            try {
                return input.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(new Function<Path, File>() {
                    @Override
                    public File apply(Path path1) {
                        return path1.toFile();
                    }
                })
                .forEach(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        file.delete();
                    }
                });
    }
}
