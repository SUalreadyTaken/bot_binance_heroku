package su.binance_bot.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import su.binance_bot.Model.Idle;
import su.binance_bot.Repository.IdleRepository;

@Service
@Transactional
public class IdleService {

	private final IdleRepository idleRepository;

	public IdleService(IdleRepository idleRepository) {
		this.idleRepository = idleRepository;
	}

	public boolean getAlternativeBoolean() {
		return idleRepository.findAll().get(0).isAlternative();
	}

	public void switchAlternativeBoolean() {
		Idle currentIdle = idleRepository.findAll().get(0);
		currentIdle.setAlternative(!currentIdle.isAlternative());
		idleRepository.save(currentIdle);
	}
}
