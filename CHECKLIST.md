# poke-cli Learning Checklist

Assessment of current codebase (JDK 26, JUnit 6, Jackson 2). Items reflect teaching-mode

# ⏸️ WHEN YOU'RE BACK — resume in this order

Build status (verified by running the suite): ✅ compiles, ✅ **8/8 tests green** —
`throwErrorForInvalidPrimaryType` fixed by you (now uses `assertThrows`).

1. **Implement the loader** in the empty `PokemonLoader` (org.example.data): Jackson 2
   `ObjectMapper` → read `pokemon.json` → build `Pokemon` + 4 `Move`s each. See "Your next
   Java work" below for the Socratic prompts (String→Type conversion, fail-fast on unknown
   types).
2. **Decide loader identity**: `PokemonFactory` (org.example.factory) is also empty — does
   "Factory" promise something a file-reader doesn't deliver? Merge or delete one.
## 🔄 Loader WIP (2026-09-17) — `PokemonLoader` v1 written by you

What's DONE (verified by reading your code):
- [x] Shape fixed: returns `List<Pokemon>`, uses `TypeReference<List<Pokemon>>` to defeat
      type erasure — the 81-entry array maps as a list (correct, idiomatic Jackson 2)
- [x] Classpath read via `getResourceAsStream` (works inside the jar — right choice)
- [x] try-with-resources on the stream
- [x] `ObjectMapper` as `private final` field (not recreated per call) ✅

Open review items (from code review — yours to fix, in order):
- [ ] **Failure policy**: both error paths return `new ArrayList<>()` — an empty list is a
      *valid-looking lie* (worse than null: silent, no crash, no clue). Replace with: throw
      when the stream is null (missing resource ≠ IOException), and either declare
      `throws IOException` (simple) or wrap in a small custom exception naming the file +
      chained cause (upgrade when constructor-deserialization errors get ugly).
- [ ] **Remove `System.err.println`** — printing is a presentation concern; it belongs in
      `Game`/`Main`, not a data class. The loader REPORTS (throws), the edge layer DECIDES
      how to tell the user.
- [ ] **Not tested yet, will fail**: `Pokemon` has no no-arg constructor, so Jackson can't
      map JSON→Pokemon yet. Write the test FIRST (assert 81 entries, Venusaur move 1 =
      Razor Leaf), watch it fail, then research **constructor-based deserialization**
      (`@JsonCreator`/`@JsonProperty` on `Pokemon` and `Move`) — your fail-fast constructors
      then become the deserialization gate for free.
- [ ] **Naming**: `loadPokemon` returns many — rename to say so once the shape is final.
- [ ] **String→Type bridge**: unknown type string in JSON (e.g. "SOUND") — decide the
      fail-fast behavior (test it).

3. Then: menu enum (kill magic numbers), extract menu from `Game`, `Battle` class design.

## 🆕 pom.xml cleaned up (2026-09-17, config only — not Java, so agent did this)
- [x] Removed the inert Jackson 3 `tools.jackson` `dependencyManagement` block (a BOM with
      no matching declared dependencies = dead weight / misleading intent)
- [x] Kept single Jackson 2 dependency: `com.fasterxml.jackson.core:jackson-databind:2.18.2`
      (latest stable 2.x; user's "2.8" was a typo — 2.8.x is 2016-era)
- [x] Removed stale `<!-- Use the latest stable version -->` comment; added an accurate one
- [ ] Yours to consider later: if you ever want Jackson's constructor parameter-name
      detection, add `maven-compiler-plugin` with the `-parameters` flag — research first

Progress since last save (all verified):
- [x] `Move.basePower` assigned in constructor ✅
- [x] `DamageCalculator` compiles and returns `int` damage; `getSTAB()` extracted ✅
- [x] `throwErrorForInvalidPrimaryType` NOW correctly uses `assertThrows` — suite is 8/8 ✅
- [x] pom.xml Jackson configuration cleaned (see above) ✅

---

review from AGENTS.md — no code is written for you; each unchecked item is yours to implement.

## ✅ Done — keep doing this

- [x] Encapsulation: `Pokemon` fields are `private final` (except mutable `currentHp`, correctly so)
- [x] No setters; mutation only via intention-revealing methods (`takeDamage()`, `fullRestore()`)
- [x] Constructor chaining: mono-type constructor delegates to dual-type via `this(...)`
- [x] `List.of()` for immutable type list; duplicate types normalized (`type1 == type2` → one entry)
- [x] `GameState` enum drives a clean state machine in `startGame()` (`while` + `switch` arrows)
- [x] Dependency injection: `Scanner` passed into `Game` constructor (testable input)
- [x] try-with-resources closes the `Scanner` in `Main`
- [x] JUnit tests with `@DisplayName` reading like behavior specs
- [x] Modern Java: `IO.println`, instance `main`, switch arrow labels

## 🔧 TODO — fix yourself, one concept at a time

### 1. Fail-fast constructor ✅ DONE
- [x] Throw `IllegalArgumentException` when `type1` is `null` — done, plus name and all four stats validated (nice: you went beyond the ask)
- [ ] Write a test using `assertThrows` proving the constructor rejects a null **primary type** —
  DONE (verified: suite is 8/8 green)
- [x] Validate `maxHp > 0` (and att/def/spd) at construction

### 2. Naming ✅ DONE
- [x] Renamed `getIsFainted()` → `isFainted()`

### 3. Kill magic numbers in the menu
- [ ] Replace raw ints `1..4` in `readMenuChoice()` / `handleMenuOption()` with a menu enum
  (pairing a label with an action — same pattern you already used for `GameState`)
- [ ] Single source of truth for valid range so adding option 5 touches one place

### 4. Single Responsibility for `Game`
- [ ] Extract menu display + input parsing out of `Game` (e.g., a `Menu`/input-reader responsibility)
- [ ] Sketch (on paper) what a `Battle` class owns: turn order, damage formula, type effectiveness
- [ ] Confirm no JSON-loading or printing logic leaks into the `model` package

### 5. State-transition hygiene
- [x] Removed the redundant `gameState = BATTLE` self-assignment in `beginBattle()`
- [ ] Decide: does only the loop or only the handler decide the next state? Make it consistent

### 6. Stubs ✅ DONE
- [x] `previewPokemon()` / `selectPokemon()` now carry intent comments

### 7. Test quality ✅ DONE (mostly)
- [x] Removed the never-failing `zeroHPCreation` test; replaced with a real `assertThrows` test
  for invalid names (verified: 6/6 tests pass)
- [x] Negative/zero-stat `assertThrows` tests written and passing
- [x] Still missing was: a *passing* `assertThrows` test for null `type1` — DONE, 8/8 green ✅

## ✅ Resolved (was WIP) — DamageCalculator now compiles
- [x] "Missing return" fixed — method returns `(int) Math.floor(rawDamage)`
- [x] Naming/void conflict resolved: returns an `int` damage value
- [x] Three locals now used: power × attStat / defStat / 50 + 2, × STAB, floored
- [x] Kept `static` + stateless for now — revisit when you add crit rolls / type chart
- [ ] Open (carried): feature-envy question — should damage calc live nearer to the data,
  or in a `Battle` that orchestrates? Decide when you flesh out the empty `Battle` class.

## 🧭 Design decisions made

### pokemon.json is the single source of truth (your call — reasonable for now)
- [x] 81 Pokémon × 4 moves embedded in `src/main/resources/pokemon.json` (324 moves, validated)
- [x] Move JSON keys mirror `Move`'s constructor exactly: `name`, `maxPP`, `basePower`, `type` — so Jackson maps cleanly
- [x] `currentPP` deliberately NOT in the JSON: mutable battle state doesn't belong in a source of truth; your constructor already sets it from `maxPP`
- [x] Only damaging moves embedded (your `Move` validation requires `basePower > 0`) — status moves (Transform, Sleep Powder…) are unrepresentable in your model yet. Ditto's 4 moves are a placeholder (Struggle + filler); decide how to handle status/non-damaging moves later.
- [x] Fixed invalid JSON: trailing comma after Gardevoir entry made the whole file unparsable

### Trade-off you accepted (revisit when bored)
- Shared moves are duplicated per Pokémon (e.g. Tackle defined on many mons with identical data).
  Nerfing Tackle later = editing N places. If that hurts, the fix is a top-level move catalog
  with Pokémon entries referencing moves by NAME — still one file, one loader.

## 🎯 Your next Java work (no code from me — this is the learning part)
- [x] **BUG:** `Move.basePower` now assigned in constructor — DONE
- [x] **Test:** `throwErrorForInvalidPrimaryType` now uses `assertThrows` — DONE (8/8 green)
- [ ] ONE loader class using Jackson: read the array, map each entry to `Pokemon` + its 4 `Move`s.
  Note: the JSON doesn't eliminate factory/loader code — it only removes the *data* duplication.
  You still write the object construction. `PokemonFactory` scaffold exists — is that the right
  name? What does "Factory" promise vs. what a file-reading class actually does?
- [ ] Where should the loader live — model? A new `data` or `factory` package? Why?
- [ ] Socratic: `Type` in JSON arrives as a String — who converts it to your `Type` enum, and what happens if the file contains a type your enum doesn't have (fail-fast again)?
- [ ] Socratic: `Pokemon` has `getType()` returning the list — but which type does STAB compare
  against for a dual-type? Test the STAB logic before trusting it.

## 🧭 Open design questions
- Where does type-effectiveness data live — in the `Type` enum, in `Move`, or in `Battle`?
- When two Pokémon share a move, does `Pokemon` calculate its own damage or does `Battle`?

