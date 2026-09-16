# poke-cli Learning Checklist

Assessment of current codebase (JDK 26, JUnit 6, Jackson). Items reflect teaching-mode
# ⏸️ WHEN YOU'RE BACK — resume in this order

1. **Fix `Move.basePower`** (5 min): constructor validates it but never assigns it.
   Add the missing assignment, then write a test: create a Move, assert `getBasePower()`
   equals what you passed in. Without this, ALL JSON move powers are silently 0.
2. **Finish `DamageCalculator`** — it currently doesn't compile (missing return).
   Decide first: does it RETURN an int damage value, and is it still `static`?
   (see 🚧 WIP section below for the Socratic questions)
3. **Test suite green**: run `JAVA_HOME=~/.jdks/openjdk-26.0.2.1 mvn test`
   (shell's default java is 25 — must point at JDK 26 or compile fails with
   "invalid target release: 26").
4. **Write the ONE loader** (Jackson): pokemon.json → `Pokemon` + 4 `Move`s each.
   Think about the name/location question in "🎯 Your next Java work" before coding.
5. Then: menu enum (kill magic numbers), extract menu from `Game`, null-type1 test.

Build status at save time: ❌ compile error in `battle/DamageCalculator.java` only;
model + tests are clean (6/6 passing when that file is out of the way).

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
- [ ] Write a test using `assertThrows` proving the constructor rejects a null **primary type** and **non-positive stats** (your current test only covers the name)
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
- [ ] Still missing: `assertThrows` tests for null `type1` and zero/negative stats (see item 1)

## 🚧 Work in progress — currently breaks the build

### DamageCalculator (new `battle` package) — DOES NOT COMPILE
- [ ] Fix "missing return statement" at line 9 (the method body computes three locals and stops)
- [ ] Concept conflict: the method is named `calculateDamage` (implies returning a value)
  but declared `void`. Which should it be? Decide *who consumes the result* and *how the battle
  learns about it* before writing more.
- [ ] Three locals (`attStat`, `defStat`, `power`) are assigned but never used — the compiler
  is telling you the algorithm isn't written yet.
- [ ] Socratic: you made it `static` — a stateless utility. Is that the right model, or will damage
  calculation need state (rng rolls, type-effectiveness table)? Also: does `battle` reaching into
  `model` getters (`getAtt`, `getDef`) smell like feature envy? Consider whether the calculation
  belongs closer to the data.

> NOTE: because this file doesn't compile, the whole suite cannot run until it's finished.
> Temporarily setting it aside was how the 6 model tests were verified — don't merge/commit
> with a broken build.

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
- [ ] **BUG first:** `Move.basePower` is never assigned in your constructor (`this.basePower = basePower` is missing) — `getBasePower()` always returns 0 and `DamageCalculator` would compute zero damage no matter what the JSON says. Fix + test.
- [ ] ONE loader class using Jackson: read the array, map each entry to `Pokemon` + its 4 `Move`s. Note: the JSON doesn't eliminate factory/loader code — it only removes the *data* duplication. You still write the object construction.
- [ ] Socratic: should the loader be a `PokemonRepository`, a `PokemonLoader`, or a `PokemonFactory`? What does the name promise? Where does it live (model? a new `data` package?) and why?
- [ ] Socratic: `Type` in JSON arrives as a String — who converts it to your `Type` enum, and what happens if the file contains a type your enum doesn't have (fail-fast again)?
- [ ] Then finish `DamageCalculator` (still doesn't compile — see WIP section above).

## 🧭 Open design questions
- Where does type-effectiveness data live — in the `Type` enum, in `Move`, or in `Battle`?
- When two Pokémon share a move, does `Pokemon` calculate its own damage or does `Battle`?

