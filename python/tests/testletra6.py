d = {
	"Love Actually ": "Richard Curtis",
	"Kill Bill": "Tarantino",
	"Amelie": "Jean-Pierre Jeunet"
}
print d
print d["Love Actually "] # devuelve "Richard Curtis"
d["Kill Bill"] = "Quentin Tarantino"
print d

t = (1, 2, True, "python")
print type(t), " ", t
t = (1)
print type(t), " ", t
t = (1,)
print type(t), " ", t

t = (1, 2, True, "python")
mi_var = t[0] # mi_var es 1
print mi_var
mi_var = t[0:2] # mi_var es (1, 2)
print mi_var
