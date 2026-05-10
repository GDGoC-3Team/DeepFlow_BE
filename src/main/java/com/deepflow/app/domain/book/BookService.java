package com.deepflow.app.domain.book;

import com.deepflow.app.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Book selectDailyBook(User user) {
        // TODO: Daily book selection must exclude books already read by user. If all books have been read, reset and provide randomly.
        List<Book> candidates = bookRepository.findUnreadBooks(user);
        if (candidates.isEmpty()) {
            candidates = bookRepository.findAll();
        }
        if (candidates.isEmpty()) {
            throw new EntityNotFoundException("Book not found");
        }
        return candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
    }
}
