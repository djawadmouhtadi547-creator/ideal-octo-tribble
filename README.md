# THE LAST PLAYER

Minecraft Java **1.16.5 / Forge 36.2.39** psychological horror + drama mod.
You find a hidden room, switch on a strange device, and meet **ECHO** - a small floating creature of blue glass
with a light in its chest. He is funny and kind, and slowly you learn what he is afraid of.

> Main scenes are fully scripted and always happen. The AI (any OpenAI-compatible API) only handles side chat,
> and is filtered so it can never break the story. No API key? ECHO falls back to written lines.

## Install / play
1. Install Forge 1.16.5 (36.2.39+), drop `TheLastPlayer-1.16.5-*.jar` in `mods/`.
2. New world. After ~3 minutes a faint signal appears in the action bar pointing to a **blue glass pillar**. Open the hatch next to it, climb down, right-click the device.
3. Talk to ECHO in normal chat when he is near. Click the choices that appear in chat.

## AI setup (optional)
Run once, then edit `config/thelastplayer-common.toml`:
```
[ai]
  endpoint = "https://api.openai.com/v1/chat/completions"   # or OpenRouter / Groq / Ollama
  model = "gpt-4o-mini"
  apiKey = ""        # leave empty and set env var ECHO_API_KEY instead (recommended)
```
Never commit your key. The key is only used server-side (singleplayer = your PC).

## Story at a glance
| Day | What happens |
|---|---|
| 1 | Device, ECHO wakes, gifts/tips, dusk question "هل أنت دائمًا تلعب وحدك؟" |
| 2 | ECHO acts strange, stares at the sky, asks if you'd take him to a new world. Hidden sector rooms + old terminals (PROJECT ECHO) |
| 3 | ECHO vanishes. A book gives the lab coordinates. Read the logs (`SESSION ENDED - SUBJECT ABANDONED`) |
| 4 | Return home: ECHO is waiting. Comfort / reject / keep searching. Then the last room of the lab |

Endings: **saved** (secret, needs shard + developer letter + hidden memory log, and a strong bond), **lost** (protocol fails),
**abandoned** (you leave / time runs out), **farewell** (you stay beside him). After a non-saved ending, go back to the device room...

## Build
GitHub Actions builds the JAR on every push (Actions tab -> artifact `TheLastPlayer-jar`). Tag `v0.1.0` to publish it as a release.
Local: `gradle build` with JDK 8 (or `gradle genEclipseRuns` / `genIntellijRuns` for dev).

## Testing commands (ops)
`/echo dev day <1-4>` · `/echo dev tp <device|a|b|lab|final>` · `/echo dev items` · `/echo dev unlockfinal` · `/echo dev bond <n>` · `/echo dev status`

## Project layout
See `docs/ARCHITECTURE.md`.
