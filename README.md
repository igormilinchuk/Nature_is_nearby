# Природа поруч

Android-застосунок, який показує свіжі спостереження тварин і рослин з iNaturalist поблизу вас і дозволяє зібрати власний список видів.

## Екрани

1. **Каталог** — стрічка спостережень із фото, назвою виду та місцем.
2. **Деталі** — картка виду з великим фото, датою, місцем і координатами.
3. **Мій список** — види, збережені на пристрої.
4. **Налаштування** — радіус пошуку та тема оформлення.

## Джерело даних

[iNaturalist API v1](https://api.inaturalist.org/v1/docs/), ключ не потрібен.

```
GET https://api.inaturalist.org/v1/observations?taxon_name=Vulpes&per_page=20&photos=true
```

- Масив об'єктів: `results[]`
- Зображення: `results[].photos[0].url` — мале квадратне (`square`); для більших розмірів суфікс замінюється на `medium` або `large`
- Корисні поля: `id`, `species_guess`, `taxon.name`, `taxon.preferred_common_name`, `place_guess`, `observed_on`, `location`
- Ліміт: рекомендовано не більше ~60 запитів на хвилину (≈1 запит/с) і до 10 000 на добу

## Зберігається на пристрої

- Власний список видів
- Радіус пошуку

## Запити до API (ЛР 7)

- Список: `GET /v1/observations?lat=50.4501&lng=30.5234&radius=50&photos=true&order_by=observed_on&per_page=30&locale=uk`, а для обраної групи додається `iconic_taxa=Mammalia` (Aves, Insecta, Plantae, Fungi).
- Деталі: `GET /v1/observations/{id}?locale=uk`.
- Ключ API не потрібен, тож у `local.properties` нічого дописувати не треба.

## Технології

Kotlin, Jetpack Compose, Material 3, Navigation Compose, DataStore, Retrofit, kotlinx.serialization, Gradle Kotlin DSL.
