# Conway's Game of Life
A legismertebb sejtautomata. Minden cella két állapotban lehet (élő/üres), és a következő iterációban azok a cellák maradnak életben, amelyeknek 2 vagy 3 élő szomszédja van, és azok az üres cellákban kelnek életre, amelyeknek pontosan 3 szomszédja van (Moore-környezetben, tehát a környező 8 cellából).

## Elérhető pályák
### Négyzetrács
Moore környezet, 8 szomszédja van minden négyzetnek a rácson.
Méret: 85x85
### Hatszögrács
Az élszomszédai a szomszédok, azaz 6 szomszédja van. Minden második sor nullától kezdve el van tolva jobbra.
Méret: 85x85
### Háromszögrács
Az élszomszédai a szomszédok, 3 szomszédja van. Felfelé néző háromszöget követ egy lefelénéző, majd így tovább a sor végéig. Minden második sor fordított állású.
Méret: 57x115

## Funkciók
- Új játék
- Játék betöltése

- Pálya randomizálása
- Szimuláció sebességének csökkentése/növelés
- Indítás/megállítás
- A cellák élő állapotból halottba színátmenettel menjen.